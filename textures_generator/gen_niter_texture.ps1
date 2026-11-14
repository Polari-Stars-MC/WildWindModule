# 生成硝石（niter）物品贴图：以灰烬堆的轮廓为底，替换为浅色硝石粉末并点缀淡黄色晶体。
# 用法：pwsh textures_generator/gen_niter_texture.ps1
Add-Type -AssemblyName System.Drawing

$root = Split-Path -Parent $PSScriptRoot
$src = Join-Path $root "src/barren/resources/assets/ww_barren/textures/item/ash.png"
$dst = Join-Path $root "src/barren/resources/assets/ww_barren/textures/item/niter.png"

$palette = @{
    0x474140 = 0x6B675F
    0x343030 = 0x8D897F
    0x685C5A = 0xAAA69C
    0x7F7573 = 0xC7C3B9
    0x8F807C = 0xDDD9CF
    0x9E8F8B = 0xF3F0E7
}

$ash = New-Object System.Drawing.Bitmap($src)
$niter = New-Object System.Drawing.Bitmap(16, 16, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)

for ($y = 0; $y -lt 16; $y++) {
    for ($x = 0; $x -lt 16; $x++) {
        $pixel = $ash.GetPixel($x, $y)
        if ($pixel.A -eq 0) {
            $niter.SetPixel($x, $y, [System.Drawing.Color]::Transparent)
            continue
        }
        $rgb = ($pixel.R -shl 16) -bor ($pixel.G -shl 8) -bor $pixel.B
        $mapped = $palette[$rgb]
        if ($null -eq $mapped) {
            $mapped = 0xC7C3B9
        }
        $niter.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, ($mapped -shr 16) -band 0xFF, ($mapped -shr 8) -band 0xFF, $mapped -band 0xFF))
    }
}

# 淡黄色晶体点缀，避免整体看起来只是灰烬换色
$specks = @(
    @(6, 4), @(8, 6), @(5, 7), @(9, 8), @(7, 9), @(10, 10)
)
foreach ($speck in $specks) {
    $x = $speck[0]
    $y = $speck[1]
    if ($niter.GetPixel($x, $y).A -gt 0) {
        $niter.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, 0xF2, 0xE6, 0xB0))
    }
}

$niter.Save($dst, [System.Drawing.Imaging.ImageFormat]::Png)
$niter.Dispose()
$ash.Dispose()

Write-Host "generated $dst"
