# Essential Right UI

Add-on Fabric (MC 26.2, mojmap) untuk **Essential 1.5.2**. Jar Essential tidak diubah; semua lewat mixin.

Title screen saja:
- tombol vanilla MC pindah ke kiri (`TitleScreenMixin`, priority 500 supaya jalan sebelum Essential baca posisi tombol)
- kolom tombol Essential nempel di tepi kanan (`PauseMenuDisplayMixin` → `getRightSideMenuX`)
- kolom player/wardrobe Essential pindah ke kiri kolom kanan (`LeftSideBarMixin`)

Atur jarak di `RightUiConfig.java`. Pause menu tidak disentuh.

## Build
`gradle build` (JDK 25). CI: `.github/workflows/build.yml` (branch `main`).
`libs/elementa-787.jar` = compile-only (Elementa, MIT); saat runtime datang dari Essential.

## Kalau Essential update
Target mixin (hasil javap 1.5.2): `PauseMenuDisplay.getRightSideMenuX(Window,UIContainer,WidthConstraint)`,
`PauseMenuDisplay.initContent(Screen)`, ctor `LeftSideBar` (6 param). `defaultRequire: 1` → crash jelas kalau berubah.
