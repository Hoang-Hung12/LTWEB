// HÀM MỞ MODAL SỬA SÂN (Dùng trong file pitch.html)
function editPitch(id) {
    const myModal = new bootstrap.Modal(document.getElementById('editPitchModal'));
    myModal.show();
}

// HÀM MỞ MODAL XÓA SÂN (Dùng trong file pitch.html)
function deleteItem(name) {
    document.getElementById('deleteItemName').innerText = "Đang xóa: " + name;
    const myModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    myModal.show();
}

// Script dùng chung admin: không vẽ chart cứng tại đây.
// Từng trang dashboard/thống kê sẽ tự vẽ chart bằng dữ liệu API riêng.