# 用原版「可疑的沙」作基板，按灵魂沙的明暗色阶重新上色，生成可疑的灵魂沙 4 帧贴图。
# 用法：pwsh -File gen_suspicious_soul_sand.ps1 -VanillaDir <解压出的原版 block 贴图目录>
param(
    [Parameter(Mandatory = $true)][string]$VanillaDir,
    [string]$OutDir = (Join-Path $PSScriptRoot '..\src\barren\resources\assets\ww_barren\textures\block')
)

Add-Type -AssemblyName System.Drawing

function Get-Pixels([string]$Path) {
    $bmp = [System.Drawing.Bitmap]::new($Path)
    $px = New-Object 'System.Drawing.Color[,]' $bmp.Width, $bmp.Height
    for ($y = 0; $y -lt $bmp.Height; $y++) {
        for ($x = 0; $x -lt $bmp.Width; $x++) {
            $px[$x, $y] = $bmp.GetPixel($x, $y)
        }
    }
    $bmp.Dispose()
    , $px
}

function Get-Luminance([System.Drawing.Color]$Color) {
    0.299 * $Color.R + 0.587 * $Color.G + 0.114 * $Color.B
}

function Lerp-Channel([int]$From, [int]$To, [double]$T) {
    [int][Math]::Round($From + ($To - $From) * $T)
}

# 灵魂沙的暗色 / 亮色（取整张贴图的最小 / 最大亮度像素）
$soulPixels = Get-Pixels (Join-Path $VanillaDir 'soul_sand.png')
$sorted = $soulPixels | Sort-Object { Get-Luminance $_ }
$dark = $sorted[0]
$light = $sorted[-1]

# 4 帧可疑沙的公共亮度范围，保证 4 张图色调一致
$frames = @()
$minL = [double]::MaxValue
$maxL = [double]::MinValue
foreach ($i in 0..3) {
    $pixels = Get-Pixels (Join-Path $VanillaDir "suspicious_sand_$i.png")
    $frames += , $pixels
    foreach ($c in $pixels) {
        $l = Get-Luminance $c
        if ($l -lt $minL) { $minL = $l }
        if ($l -gt $maxL) { $maxL = $l }
    }
}

foreach ($i in 0..3) {
    $pixels = $frames[$i]
    $bmp = [System.Drawing.Bitmap]::new(16, 16, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    for ($y = 0; $y -lt 16; $y++) {
        for ($x = 0; $x -lt 16; $x++) {
            $t = if ($maxL -gt $minL) { ((Get-Luminance $pixels[$x, $y]) - $minL) / ($maxL - $minL) } else { 0.5 }
            $color = [System.Drawing.Color]::FromArgb(
                255,
                (Lerp-Channel $dark.R $light.R $t),
                (Lerp-Channel $dark.G $light.G $t),
                (Lerp-Channel $dark.B $light.B $t)
            )
            $bmp.SetPixel($x, $y, $color)
        }
    }
    $target = Join-Path $OutDir "suspicious_soul_sand_$i.png"
    $bmp.Save($target, [System.Drawing.Imaging.ImageFormat]::Png)
    $bmp.Dispose()
    Write-Host "written: $target"
}
