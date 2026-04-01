Tugas Pemerograman Mobile 1

# Kamael Store - Prototip Aplikasi Top Up Game

Kamael Store adalah aplikasi Android berbasis **Jetpack Compose** yang dirancang sebagai prototip platform top up currency game. Proyek ini dibuat untuk memenuhi tugas mata kuliah pemrograman mobile 1 dengan mengimplementasikan berbagai komponen UI dan validasi data yang kompleks.

## 🚀 Fitur Utama

Aplikasi ini mencakup fitur-fitur sesuai dengan indikator tugas:

1.  **Sistem Login & Registrasi**: Alur autentikasi sederhana untuk masuk ke dalam store.
2.  **Formulir Lengkap**: Input data registrasi menggunakan `OutlinedTextField` (Nama, Email, Password, Konfirmasi Password).
3.  **Validasi Lanjutan**:
    *   Validasi real-time (pesan error muncul saat mengetik).
    *   Cek format email dan minimal karakter password.
    *   Fitur *Password Match* untuk memastikan keamanan data.
    *   Tombol submit hanya aktif jika semua kriteria validasi terpenuhi.
4.  **Selection Controls**:
    *   **RadioGroup**: Pemilihan Jenis Kelamin.
    *   **Checkbox**: Pemilihan Hobi (dengan validasi minimal 3 pilihan).
5.  **Spinner & Dialog**:
    *   **Custom Spinner**: Menggunakan `ExposedDropdownMenuBox` untuk memilih Tipe Akun (Gamer, Reseller, Member) dan Nominal Top Up.
    *   **Confirmation Dialog**: Alert Dialog muncul untuk konfirmasi sebelum data dikirim (Register & Top Up).
6.  **Gesture Interaction**:
    *   Implementasi **Long Press** pada tombol utama untuk memicu aksi tambahan berupa reset/clear form.

## 🛠️ Teknologi yang Digunakan

*   **Bahasa**: [Kotlin](https://kotlinlang.org/)
*   **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Modern Toolkit)
*   **Design System**: Material Design 3
*   **Version Control**: Git

## 📸 Cara Menjalankan
1.  Clone repository ini:
    ```bash
    git clone https://github.com/adrianAsh199/Kamael-Store-Prototype.git
    ```
2.  Buka proyek di **Android Studio (Ladybug atau versi terbaru)**.
3.  Tunggu proses Gradle Sync selesai.
4.  Jalankan aplikasi di Emulator atau perangkat fisik Android.

## 👤 Author
*   **Adrian** - [adrianAsh199](https://github.com/adrianAsh199)

---
*Proyek ini dikembangkan untuk tujuan pembelajaran pengembangan aplikasi Android.*
