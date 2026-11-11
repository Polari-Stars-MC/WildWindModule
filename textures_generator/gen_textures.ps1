Add-Type -AssemblyName System.Drawing

$core = 'D:\newmod\WildWindModule\src\core\resources\assets\ww_core\textures'
$bar  = 'D:\newmod\WildWindModule\src\barren\resources\assets\ww_barren\textures'
$van  = 'D:\newmod\WildWindModule\build\tmp\mcsrc26\assets\minecraft\textures\block'

foreach ($d in @('block', 'item', 'entity\boat', 'entity\chest_boat', 'gui\signs', 'gui\hanging_signs')) {
    New-Item -ItemType Directory -Force -Path "$bar\$d" | Out-Null
}

# cinder = 焚烬木(blaze)，ember = 灵焰木(soul)
$pairs = @(
    @("$core\entity\boat\cinder.png",        "$bar\entity\boat\blaze.png"),
    @("$core\entity\boat\ember.png",         "$bar\entity\boat\soul.png"),
    @("$core\entity\chest_boat\cinder.png",  "$bar\entity\chest_boat\blaze.png"),
    @("$core\entity\chest_boat\ember.png",   "$bar\entity\chest_boat\soul.png"),
    @("$core\item\cinder_boat.png",          "$bar\item\blaze_boat.png"),
    @("$core\item\ember_boat.png",           "$bar\item\soul_boat.png"),
    @("$core\item\cinder_chest_boat.png",    "$bar\item\blaze_chest_boat.png"),
    @("$core\item\ember_chest_boat.png",     "$bar\item\soul_chest_boat.png"),
    @("$core\item\cinder_sign.png",          "$bar\item\blaze_sign.png"),
    @("$core\item\ember_sign.png",           "$bar\item\soul_sign.png"),
    @("$core\item\cinder_hanging_sign.png",  "$bar\item\blaze_hanging_sign.png"),
    @("$core\item\ember_hanging_sign.png",   "$bar\item\soul_hanging_sign.png"),
    @("$core\gui\hanging_signs\cinder.png",  "$bar\gui\hanging_signs\blaze.png"),
    @("$core\gui\hanging_signs\ember.png",   "$bar\gui\hanging_signs\soul.png")
)
foreach ($p in $pairs) {
    Copy-Item -Force $p[0] $p[1]
}

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

function ScaleTo {
    param([string]$SrcPath, [int]$W, [int]$H, [string]$OutPath)
    $src = [System.Drawing.Bitmap]::new($SrcPath)
    $out = [System.Drawing.Bitmap]::new($W, $H, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    for ($x = 0; $x -lt $W; $x++) {
        for ($y = 0; $y -lt $H; $y++) {
            $sx = [int][math]::Floor($x * $src.Width / $W); if ($sx -ge $src.Width) { $sx = $src.Width - 1 }
            $sy = [int][math]::Floor($y * $src.Height / $H); if ($sy -ge $src.Height) { $sy = $src.Height - 1 }
            $out.SetPixel($x, $y, $src.GetPixel($sx, $sy))
        }
    }
    $out.Save($OutPath, [System.Drawing.Imaging.ImageFormat]::Png)
    $src.Dispose(); $out.Dispose()
}

# 方块贴图：以原版橡木对应贴图的明暗为准，套用本模组木材底色
Recolor "$van\oak_sign.png"         "$bar\block\blaze_planks.png"       32 32 "$bar\block\blaze_sign.png"
Recolor "$van\oak_sign.png"         "$bar\block\soul_planks.png"        32 32 "$bar\block\soul_sign.png"
Recolor "$van\oak_hanging_sign.png" "$bar\block\stripped_blaze_log.png" 32 32 "$bar\block\blaze_hanging_sign.png"
Recolor "$van\oak_hanging_sign.png" "$bar\block\stripped_soul_log.png"  32 32 "$bar\block\soul_hanging_sign.png"
Recolor "$van\oak_shelf.png"        "$bar\block\blaze_planks.png"       32 32 "$bar\block\blaze_shelf.png"
Recolor "$van\oak_shelf.png"        "$bar\block\soul_planks.png"        32 32 "$bar\block\soul_shelf.png"

# 告示牌编辑界面背景（原版尺寸 24x26 / 16x16）
ScaleTo "$bar\block\blaze_planks.png" 24 26 "$bar\gui\signs\blaze.png"
ScaleTo "$bar\block\soul_planks.png"  24 26 "$bar\gui\signs\soul.png"

Get-ChildItem $bar -Recurse -File -Filter *.png | Where-Object {
    $_.Name -match '^(blaze|soul)_(sign|hanging_sign|shelf|boat|chest_boat)\.png$' -or $_.DirectoryName -match 'entity|gui'
} | ForEach-Object { "{0}  {1:N0}" -f $_.FullName.Replace("$bar\", ''), $_.Length }
