Add-Type -AssemblyName System.Drawing

# 杜鹃木专属方块贴图：告示牌 / 悬挂告示牌 / 展示架
# 做法与 gen_textures.ps1 一致：以原版橡木对应贴图的明暗为准，套用本模组木材底色
# 用法：在仓库根目录执行 pwsh -NoProfile -File textures_generator\gen_azalea_textures.ps1

$core = 'D:\newmod\WildWindModule\src\core\resources\assets\ww_core\textures'
$van  = 'D:\newmod\WildWindModule\build\tmp\mcsrc26\assets\minecraft\textures\block'

function Recolor {
    param([string]$RefPath, [string]$BasePath, [int]$W, [int]$H, [string]$OutPath)
    $ref = [System.Drawing.Bitmap]::new($RefPath)
    $base = [System.Drawing.Bitmap]::new($BasePath)
    $out = [System.Drawing.Bitmap]::new($W, $H, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    $sum = 0.0
    $n = 0
    for ($x = 0; $x -lt $W; $x++) {
        for ($y = 0; $y -lt $H; $y++) {
            $c = $ref.GetPixel($x, $y)
            if ($c.A -gt 16) { $sum += (0.299 * $c.R + 0.587 * $c.G + 0.114 * $c.B); $n++ }
        }
    }
    $avg = if ($n -gt 0) { $sum / $n } else { 1.0 }
    for ($x = 0; $x -lt $W; $x++) {
        for ($y = 0; $y -lt $H; $y++) {
            $r = $ref.GetPixel($x, $y)
            if ($r.A -le 16) {
                $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0))
                continue
            }
            $bx = [int][math]::Floor($x * $base.Width / $W); if ($bx -ge $base.Width) { $bx = $base.Width - 1 }
            $by = [int][math]::Floor($y * $base.Height / $H); if ($by -ge $base.Height) { $by = $base.Height - 1 }
            $b = $base.GetPixel($bx, $by)
            $lum = 0.299 * $r.R + 0.587 * $r.G + 0.114 * $r.B
            $k = if ($avg -gt 0) { $lum / $avg } else { 1.0 }
            $nr = [int][math]::Min(255, [math]::Max(0, [math]::Round($b.R * $k)))
            $ng = [int][math]::Min(255, [math]::Max(0, [math]::Round($b.G * $k)))
            $nb = [int][math]::Min(255, [math]::Max(0, [math]::Round($b.B * $k)))
            $out.SetPixel($x, $y, [System.Drawing.Color]::FromArgb($r.A, $nr, $ng, $nb))
        }
    }
    $out.Save($OutPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $ref.Dispose(); $base.Dispose(); $out.Dispose()
}

Recolor "$van\oak_sign.png"         "$core\block\azalea_planks.png"        32 32 "$core\block\azalea_sign.png"
Recolor "$van\oak_hanging_sign.png" "$core\block\stripped_azalea_log.png"  32 32 "$core\block\azalea_hanging_sign.png"
Recolor "$van\oak_shelf.png"        "$core\block\azalea_planks.png"        32 32 "$core\block\azalea_shelf.png"

Get-ChildItem "$core\block" -File -Filter "azalea_*.png" |
    Where-Object { $_.Name -match 'azalea_(sign|hanging_sign|shelf)\.png$' } |
    ForEach-Object { "{0}  {1:N0}" -f $_.FullName.Replace("$core\", ''), $_.Length }
