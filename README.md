https://eshop-advprog-sahilaathia.koyeb.app/

<details><summary><b>Module 01 - Coding Standards</b></summary>

# Reflection 1

**Clean Code Principles dan Secure Coding Practices yang Telah Diterapkan:**
- Separation of concerns: Kode dipisahkan ke dalam package yang berbeda (model, repository, service, controller) sesuai tanggung jawabnya masing-masing, sehingga lebih mudah dipahami dan di-maintain.
- Meaningful names: Nama yang digunakan deskriptif untuk class, method, dan variable. Contoh: ProductController, createProduct(), dan productList yang langsung menjelaskan fungsinya tanpa perlu comment tambahan.
- Focused functions: Setiap method yang dibuat fokus untuk melakukan satu tugas saja. Misalnya createProductPage() hanya menampilkan halaman form, sedangkan createProductPost() khusus untuk memproses data produk baru.
- Separation of logic: Business logic dipisahkan di service layer, tidak langsung di controller, sehingga lebih aman dan mudah untuk ditambahkan security check di masa depan.
- UUID generation: Menggunakan java.util.UUID untuk membuat ID produk secara acak dan unik, sehingga user tidak bisa menebak ID produk lain hanya dengan mencoba angka yang berurutan di URL.

**Improvement yang Bisa Dilakukan:**
- Error Handling: Saat ini error handling masih minimal. Perlu ditambahkan try-catch dan validasi untuk handle edge cases seperti product dengan ID yang tidak ada saat edit/delete.
- Input Sanitization: Melakukan pembersihan pada input teks untuk memastikan tidak ada tag HTML atau script berbahaya yang dimasukkan oleh user melalui form.


# Reflection 2

### (1)
- Menulis unit test membuat saya merasa lebih yakin dengan kode yang saya buat dan lebih tenang saat development. Unit test membantu saya memastikan setiap method sudah bekerja sesuai ekspektasi.
- Menurut saya, jumlah unit test yang perlu dibuat untuk suatu class tidak menentu, yang penting minimal setiap method public harus memiliki satu test untuk positive dan negative scenario. Untuk method yang kompleks, mungkin perlu lebih banyak test untuk cover berbagai case.
- Untuk memastikan unit test yang dibuat sudah cukup atau belum, bisa gunakan code coverage sebagai pengukur.
- Akan tetapi, 100% code coverage tidak menjamin kode bebas dari bug atau error, karena code coverage hanya mengukur baris kode yang dijalankan, bukan apakah logika tersebut sudah benar dalam menangani semua kemungkinan skenario.

### (2)
- Membuat functional test suite baru dengan setup procedures dan instance variables yang sama akan menurunkan code quality karena terjadi code duplication.
- Potensi masalah clean code:
  - Violation of DRY Principle: Setup WebDriver dan konfigurasi yang sama akan ditulis berulang di setiap test class
  - Redundant Code: Instance variables dan setup method akan ter-duplicate di semua functional test suite
  - Poor Maintainability: Jika ada perubahan konfigurasi, harus mengubah di banyak file
- Saran improvement adalah menerapkan prinsip DRY (Don't Repeat Yourself) dengan membuat sebuah base class yang menampung prosedur setup umum, sehingga test class lainnya cukup inherit dari base class tersebut.
</details>


<details><summary><b>Module 02 - CI/CD & DevOps</b></summary>

# Reflection

### 1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

Setelah melakukan analisis menggunakan tool PMD, saya menemukan dan memperbaiki beberapa code quality issues sebagai berikut:
- JUnit tests should include assert or fail: Isu ini terdapat pada `EshopApplicationTests.java`. Saya memperbaikinya dengan menambahkan `assertDoesNotThrow` untuk memanggil metode main aplikasi, sehingga dapat terverifikasi bahwa aplikasi dapat di-load tanpa menimbulkan exception.
- Avoid duplication of string literals: Banyak string yang digunakan berulang kali di dalam file test (seperti ID produk dan nama produk). Saya memperbaiki isu ini dengan mengextract string-string tersebut menjadi constant `private static final String` untuk mematuhi prinsip DRY (Don't Repeat Yourself) dan memudahkan pemeliharaan kode kedepannya.
- Unit tests should not contain more than 1 assert(s): Beberapa test methods memiliki lebih dari satu assertion. Saya menggunakan `assertAll()` untuk mengelompokkannya sehingga assertion count dapat dikurangi.
- JUnit assertions should include a message: PMD mendeteksi banyak assertion yang tidak memiliki pesan deskriptif. Sehingga saya menambahkan argumen message di setiap fungsi assertion agar kegagalan tes lebih mudah diidentifikasi penyebabnya.

### 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment?

Menurut saya, implementasi CI/CD workflows pada repositori ini sudah memenuhi definisi Continuous Integration (CI) dan Continuous Deployment (CD) karena:

- Continuous Integration (CI): Setiap kali ada push atau pull request, GitHub Actions secara otomatis menjalankan workflow `ci.yml` untuk unit test, `pmd.yml` untuk analisis kualitas kode, serta `scorecard.yml` untuk security scanning, sehingga proses pengecekan dilakukan secara terus-menerus (continuous) dan menyatu dalam satu alur otomatis sebelum perubahan di-merge ke main branch (integration).

- Continuous Deployment (CD): Melalui integrasi antara `Dockerfile` dan workflow `deploy.yml`, setiap perubahan kode yang di-push ke repository akan secara otomatis membangun Docker image dan mengirimkannya ke PaaS Koyeb menggunakan approach push-based deployment. Hal ini memastikan proses merilis versi terbaru aplikasi dilakukan secara otomatis dan berkelanjutan (continuous) serta langsung menerapkan perubahan kode ke production environment  tanpa proses manual (deployment).

Kedua alur ini membentuk pipeline otomatis yang menjamin setiap perubahan kode terintegrasi dan tervalidasi dengan baik serta memastikan bahwa versi terbaru aplikasi selalu ter-deploy secara konsisten. Dengan demikian, main branch akan selalu berada dalam kondisi stabil, teruji, dan siap digunakan.

</details>

<details><summary><b>Module 03 - Maintainability & OO Principles</b></summary>

# Reflection

### 1) SOLID Principles Applied:

- **Single Responsibility Principle (SRP)**: Class `CarController` yang sebelumnya berada di dalam `ProductController.java` dipindahkan ke file tersendiri. Tujuannya agar `ProductController` tidak lagi menangani dua entitas berbeda sekaligus (Product dan Car). Dengan begitu, setiap controller memiliki satu tanggung jawab yang jelas dan tidak saling bercampur.

- **Liskov Substitution Principle (LSP)**: Hubungan inheritance (`extends`) antara `CarController` dan `ProductController` dihapuskan karena secara konsep `CarController` bukan turunan yang valid dari `ProductController`. Keduanya mengelola model yang berbeda, sehingga hubungan inheritance tersebut tidak tepat dan berpotensi menimbulkan inkonsistensi dalam behavior.

- **Open-Closed Principle (OCP)**: Setelah dipisahkan (tahap apply SRP dan LSP), struktur kode menjadi lebih terbuka untuk dikembangkan tanpa harus mengubah kode yang sudah ada. Misalnya, jika nanti ingin menambahkan jenis kendaraan baru seperti motor, kita cukup membuat controller dan service baru tanpa perlu memodifikasi `CarController` atau `ProductController`. Hal ini meminimalkan risiko munculnya bug pada kode yang sudah stabil.

- **Interface Segregation Principle (ISP)**: `CarService` dan `ProductService` sudah dibuat terpisah dan spesifik sesuai kebutuhan masing-masing entitas. Dengan begitu, setiap implementation class hanya mengimplementasikan method yang memang digunakan. Pemutusan hubungan inheritance antara `CarController` dan `ProductController` juga bertujuan agar `CarController` tidak dipaksa untuk memiliki akses ke dependensi `ProductService` yang sebenarnya tidak dibutuhkannya.

- **Dependency Inversion Principle (DIP)**: Dependensi pada `CarController` juga sudah diubah agar tidak lagi langsung bergantung pada `CarServiceImpl`, melainkan pada interface `CarService`. Perubahan ini dilakukan dengan menyesuaikan tipe data pada anotasi `@Autowired`. Dengan demikian, controller sebagai modul tingkat tinggi bergantung pada abstraksi (interface), bukan pada kelas implementasi concrete. Dengan demikian kode menjadi lebih fleksibel dan mudah dikembangkan ke depannya.


### 2) Advantages of Applying SOLID Principles

- **Kemudahan Perawatan dan Debugging (SRP + LSP)**: Dengan memisahkan responsibility, melacak error jadi lebih mudah dan fokus. Contoh: Jika terjadi kesalahan pada proses penghapusan mobil, kita cukup memeriksa metode `deleteCarById` di `CarController` dan `CarService`. Tidak perlu khawatir perubahan tersebut akan merusak fitur `Product` karena kodenya sudah terisolasi di file yang berbeda.

- **Fleksibilitas dalam Pengembangan Fitur Baru (OCP)**: Sistem menjadi lebih mudah adapt perubahan kebutuhan tanpa mengganggu existing code yang sudah stabil. Contoh: Apabila nantinya ada kebutuhan untuk menambahkan validasi khusus pada `Car` (misalnya harga tidak boleh nol), hanya perlu memperbarui logic di `CarServiceImpl`. Modul lain seperti `Product` atau UI-nya tidak akan terdampak.

- **Proses Unit Testing yang Lebih Efisien (DIP + SRP)**: Struktur yang terdiri dari bagian-bagian kecil memungkinkan setiap komponen diuji secara mandiri dengan lebih mudah. Contoh: Karena `CarController` sekarang bergantung pada interface `CarService`, kita bisa menggunakan `mock` untuk melakukan testing pada Controller tanpa harus menjalankan seluruh logic di Repository. Ini membuat testing lebih cepat dan tidak rentan fail akibat masalah di layer lain.

### 3) Disadvantages of Not Applying SOLID Principles

- **Ketergantungan yang Kaku dan Rapuh (DIP)**: Tanpa abstraksi, setiap perubahan kecil pada satu bagian dapat merusak bagian lainnya unexpectedly. Contoh: Jika `CarController` langsung bergantung pada `CarServiceImpl` (concrete implementation class), maka setiap kali struktur di Service diubah, terpaksa harus mengubah banyak baris kode di Controller juga, yang meningkatkan risiko munculnya bug baru.

- **Risiko Konflik Mapping dan Perilaku Tidak Konsisten (LSP)**: Penerapan inheritance yang tidak tepat dapat menyebabkan masalah pada fungsionalitas dasar app/web. Contoh: Sebelum refactor, `CarController` inherit `ProductController`. Hal ini berisiko menyebabkan Ambiguous Mapping pada route seperti `/edit/{id}` karena aplikasi bingung menentukan apakah route tersebut milik produk atau mobil, yang bisa berujung pada kegagalan aplikasi saat dijalankan.

- **Kode yang Sulit Dibaca dan Dipahami (SRP + ISP)**: File yang "gemuk" dan mengurusi banyak hal sekaligus akan memperlambat proses development. Contoh: Jika semua logic controller tetap berada dalam satu file `ProductController.java`, developer lain akan kesulitan mencari fungsi spesifik. Selain itu, menambahkan satu field baru saja bisa memaksa kita untuk memperbarui banyak metode sekaligus di tempat yang tidak relevan.

</details>

<details><summary><b>Module 04 - Refactoring & TDD</b></summary>

# Reflection

### 1. Evaluating Testing Objectives
Alur Test-Driven Development (TDD) yang diikuti selama pengerjaan fitur Order ini sangat bermanfaat dalam menjaga kualitas kode melalui tiga aspek utama:

- **Correctness**: Menulis test terlebih dahulu di fase [RED] memaksa kita untuk memikirkan dulu requirement requirement lebih dulu sebelum nulis kode. Happy path dan unhappy path sudah dicakup dengan baik, dan edge case seperti case sensitivity pada `findAllByAuthor` juga sudah dipikirkan sejak awal. Belum ada bug yang muncul di luar ekspektasi setelah implementasi selesai, yang menunjukkan bahwa test sudah cukup merepresentasikan behavior yang diharapkan. Jika langsung menulis kode dulu tanpa test, kemungkinan besar banyak skenario-skenario test terutana unhappy path yang bakal terlewat.

- **Maintainability**: Tahap [REFACTOR] memungkinkan peningkatan desain kode tanpa merusak fungsionalitas. Misalnya seperti saat mengganti hardcoded strings dengan OrderStatus enum. Dengan adanya unit test yang sudah jalan, kita bisa refactor dengan lebih percaya diri dan hanya perlu memastikan semua test masih hijau. Tanpa test, kegiatan refactoring akan cukup berisiko.

- **Productive Workflow**: Proses ini memang butuh waktu lebih di awal karena harus memikirkan requirements dan menuliskan test dulu sebelum kodenya ada, tetapi feedback loop-nya menjadi lebih cepat. Begitu ada yang salah, langsung dapat diketahui dari hasil test, tanpa perlu debug manual satu-satu.

### 2. F.I.R.S.T. Principle
Unit test yang telah dibuat dalam tutorial ini telah memenuhi prinsip F.I.R.S.T. dengan rincian sebagai berikut:

- **Fast**: Test berjalan cepat karena hanya menguji logika internal class seperti `Order` dan `OrderRepository`. Untuk `OrderServiceImplTest`, kita pakai `Mockito` untuk mock `OrderRepository`-nya, jadi tidak ada akses ke database asli yang dapat membuat testing menjadi lambat.

- **Isolated/Independent**: Setiap test case berdiri sendiri. Method `setUp()` dengan anotasi `@BeforeEach` dipakai untuk menyiapkan data awal (seperti list `products` dan objek `Order`) dari nol sebelum setiap test jalan, jadi satu test tidak akan mempengaruhi test lainnya.

- **Repeatable**: Hasil test konsisten meskipun dijalankan berkali-kali, karena tidak bergantung pada kondisi eksternal seperti database atau waktu sistem. State yang digunakan selalu disiapkan ulang dari `setUp()`.

- **Self-Validating**: Setiap test punya assertion yang jelas. Ada `assertEquals` untuk mengecek nilai, `assertNull` untuk yang tidak ketemu, dan `assertThrows` untuk memverifikasi bahwa exception yang benar dilempar (misalnya waktu products kosong atau status tidak valid).

- **Thorough/Timely** — Test sudah thorough karena mencakup happy path dan unhappy path. Kemudian karena test ditulis sebelum implementasi kode, sesuai alur TDD yang diikuti dari awal, prinsip timely juga sudah terpenuhi.

</details>

