# datagen 产物 diff 校验（ADR-0005 迁移流程的固定动作）
#
# 用法：
#   pwsh -File tools/datagen-diff.ps1 -Snapshot            # 生成基线快照
#   pwsh -File tools/datagen-diff.ps1 -Verify              # 重跑 datagen 并与基线对比
#   pwsh -File tools/datagen-diff.ps1 -Snapshot -Modules core
#
# 为什么需要它：每次把 datagen 内容迁进 imp（ADR-0005）后，必须证明产物**逐字节不变**，
# 否则规格化会静默改变生成的 JSON。基线默认放在系统临时目录，不进入仓库。

param(
    [switch]$Snapshot,
    [switch]$Verify,
    [string[]]$Modules = @("core", "wilderness", "barren"),
    [string]$Baseline = (Join-Path $env:TEMP "ww_datagen_baseline")
)

$ErrorActionPreference = "Stop"
$repo = Split-Path $PSScriptRoot -Parent
Set-Location $repo

# Gradle 的 run task 不能一次传两个，必须逐个跑
function Invoke-Datagen([string]$module) {
    foreach ($task in @("runServerData", "runClientData")) {
        $out = & ./gradlew ":$module`:$task" --console=plain --no-configuration-cache 2>&1
        $status = ($out | Select-String "BUILD SUCCESSFUL|BUILD FAILED" | Select-Object -Last 1).Line
        if ($status -match "FAILED") {
            Write-Warning "  $module $task 失败：$status"
        } else {
            Write-Host "  $module $task 完成"
        }
    }
}

function Get-Hashes([string]$root) {
    $full = (Resolve-Path $root).Path
    Get-ChildItem $full -Recurse -File |
        Where-Object { $_.FullName -notmatch '\\\.cache\\' } |
        ForEach-Object { $_.FullName.Substring($full.Length) + '|' + (Get-FileHash $_.FullName).Hash }
}

if ($Snapshot) {
    Write-Host "生成基线快照 -> $Baseline"
    Remove-Item $Baseline -Recurse -Force -ErrorAction SilentlyContinue
    foreach ($m in $Modules) {
        Invoke-Datagen $m
        Copy-Item -Recurse "build\generated\$m" (Join-Path $Baseline $m) -ErrorAction SilentlyContinue
        $n = (Get-Hashes (Join-Path $Baseline $m) | Measure-Object).Count
        Write-Host "  基线 $m : $n 个文件"
    }
    exit 0
}

if ($Verify) {
    Write-Host "重跑 datagen 并与基线对比（基线：$Baseline）"
    $failed = 0
    foreach ($m in $Modules) {
        Invoke-Datagen $m
        $baseRoot = Join-Path $Baseline $m
        if (-not (Test-Path $baseRoot)) {
            Write-Warning "  跳过 $m ：基线不存在"
            continue
        }
        $diff = Compare-Object (Get-Hashes $baseRoot) (Get-Hashes "build\generated\$m")
        if ($diff) {
            $failed++
            Write-Host "  ❌ $m 差异 $($diff.Count) 条：" -ForegroundColor Red
            $diff | Select-Object -First 10 | ForEach-Object {
                Write-Host ("     {0} {1}" -f $_.SideIndicator, ($_.InputObject -split '\|')[0])
            }
        } else {
            Write-Host "  ✅ $m 无差异" -ForegroundColor Green
        }
    }
    if ($failed -gt 0) {
        Write-Host "有 $failed 个模块产物变化——迁移改变了输出，必须修正。" -ForegroundColor Red
        exit 1
    }
    Write-Host "全部模块产物逐字节一致。" -ForegroundColor Green
    exit 0
}

Write-Host "请指定 -Snapshot 或 -Verify。"
exit 2
