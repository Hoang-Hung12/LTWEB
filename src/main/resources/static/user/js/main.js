document.addEventListener("DOMContentLoaded", function () {
    // 1. Tự động gán link thật cho các nút trên Navbar
    const pageMap = {
        "Trang chủ": "index.html",
        "Danh sách sân": "danh-sach-san.html",
        "Lịch sử đặt": "lich-su-dat-san.html",
        "Lịch sử đặt sân": "lich-su-dat-san.html",
        "Đăng nhập": "login.html"
    };

    const navLinks = document.querySelectorAll(".nav-link, .btn-primary");

    navLinks.forEach(link => {
        const text = link.innerText.trim();
        if (pageMap[text]) {
            link.setAttribute("href", pageMap[text]);
        }
    });

    // 2. Tự động nhận diện trang hiện tại để làm sáng (Active) menu tương ứng
    // Lấy tên file hiện tại từ thanh địa chỉ (VD: lich-su-dat-san.html)
    let currentFileName = window.location.pathname.split('/').pop() || "index.html";

    // Xóa hết class active cũ (cứng) trong HTML và so sánh để gán cái mới
    document.querySelectorAll('.navbar-nav .nav-link').forEach(link => {
        link.classList.remove('active');
        
        // Lấy tên file mà thẻ link này đang trỏ tới
        let targetFileName = link.getAttribute('href'); 
        
        if (targetFileName === currentFileName) {
            link.classList.add('active');
        }
    });
});