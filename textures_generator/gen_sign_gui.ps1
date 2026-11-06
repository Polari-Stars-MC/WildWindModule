Add-Type -AssemblyName System.Drawing

# 24x26 告示牌编辑界面底板（vanilla SignEditScreen: 24x26, 板 0~11 行, 立柱 12~25 行 2px 宽）
# 取色自本模组 *_planks.png（按 4 行一带求平均），板面/立柱的像素由脚本按模板绘制：
#   3 条横板 + 深色拼缝 + 1px 描边 + 立柱，全部为本模组自绘，不引用原版贴图
# 用法：在仓库根目录执行 pwsh -NoProfile -File textures_generator\gen_sign_gui.ps1

$tex = 'src\barren\resources\assets\ww_barren\textures'
$out = "$tex\gui\signs"

function Clamp([int]$v) { if ($v -lt 0) { 0 } elseif ($v -gt 255) { 255 } else { $v } }
function Shade([System.Drawing.Color]$c, [double]$k) {
    [System.Drawing.Color]::FromArgb(255, (Clamp ([int][math]::Round($c.R * $k))), (Clamp ([int][math]::Round($c.G * $k))), (Clamp ([int][math]::Round($c.B * $k))))
}
function Jitter([System.Drawing.Color]$c, [int]$x, [int]$y) {
    $d = ((($x * 73) + ($y * 151)) % 13) - 6      # 确定性噪声，避免纯色平面
    [System.Drawing.Color]::FromArgb(255, (Clamp ($c.R + $d)), (Clamp ($c.G + $d)), (Clamp ($c.B + $d)))
}

foreach ($wood in @('cinder', 'ember')) {
    $planks = [System.Drawing.Bitmap]::FromFile((Resolve-Path "$tex\block\${wood}_planks.png").Path)

    # 每 4 行一条横板，取前 3 行（跳过拼缝行）的平均色
    $band = @()
    for ($b = 0; $b -lt 3; $b++) {
        $sr = 0; $sg = 0; $sb = 0; $n = 0
        for ($y = $b * 4; $y -lt ($b * 4 + 3); $y++) {
            for ($x = 0; $x -lt $planks.Width; $x++) {
                $c = $planks.GetPixel($x, $y); $sr += $c.R; $sg += $c.G; $sb += $c.B; $n++
            }
        }
        $band += [System.Drawing.Color]::FromArgb(255, [int]($sr / $n), [int]($sg / $n), [int]($sb / $n))
    }
    $planks.Dispose()

    $bmp = [System.Drawing.Bitmap]::new(24, 26, [System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
    for ($y = 0; $y -lt 26; $y++) { for ($x = 0; $x -lt 24; $x++) { $bmp.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(0, 0, 0, 0)) } }

    # --- 板面 rows 0..11 ---
    for ($y = 0; $y -lt 12; $y++) {
        $b = [int][math]::Floor($y / 4)
        for ($x = 0; $x -lt 24; $x++) {
            $c = Jitter $band[$b] $x $y
            if (($y % 4) -eq 3) { $c = Shade $c 0.78 }        # 拼缝
            if ($y -eq 0) { $c = Shade $c 1.14 }               # 顶面高光
            if ($y -eq 11) { $c = Shade $c 0.62 }              # 底边
            if ($x -eq 0 -or $x -eq 23) { $c = Shade $c 0.68 } # 左右描边
            $bmp.SetPixel($x, $y, $c)
        }
    }

    # --- 立柱 rows 12..25, x 11..12 ---
    $postBase = Shade $band[0] 0.86
    for ($y = 12; $y -lt 26; $y++) {
        for ($x = 11; $x -le 12; $x++) {
            $c = Jitter $postBase $x $y
            if ($x -eq 11) { $c = Shade $c 0.74 }   # 左侧暗，做出圆角感
            if ($y -eq 12) { $c = Shade $c 0.6 }    # 与板底衔接的阴影
            if ($y -eq 25) { $c = Shade $c 0.7 }
            $bmp.SetPixel($x, $y, $c)
        }
    }

    $dst = Join-Path (Resolve-Path $out).Path "$wood.png"
    $bmp.Save($dst, [System.Drawing.Imaging.ImageFormat]::Png)

    # 6x 预览
    $src = [System.Drawing.Bitmap]::FromFile($dst)
    $p = [System.Drawing.Bitmap]::new($src.Width * 6, $src.Height * 6)
    for ($y = 0; $y -lt $p.Height; $y++) {
        for ($x = 0; $x -lt $p.Width; $x++) {
            $px = [int][math]::Floor($x / 6); $py = [int][math]::Floor($y / 6)
            if ($px -ge $src.Width) { $px = $src.Width - 1 }
            if ($py -ge $src.Height) { $py = $src.Height - 1 }
            $p.SetPixel($x, $y, $src.GetPixel($px, $py))
        }
    }
    $p.Save((Join-Path (Resolve-Path 'build\tmp').Path "preview_$wood.png"), [System.Drawing.Imaging.ImageFormat]::Png)
    $p.Dispose(); $src.Dispose(); $bmp.Dispose()

    "$dst  saved"
}
