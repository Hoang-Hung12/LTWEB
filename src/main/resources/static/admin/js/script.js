// Hằng số API dùng chung cho toàn bộ trang admin (cùng origin với trang đang mở)
const API = (typeof window !== 'undefined' && window.location && window.location.origin)
    ? window.location.origin
    : 'http://localhost:8080';

// HÀM MỞ MODAL SỬA SÂN
function editPitch(id) {
    const myModal = new bootstrap.Modal(document.getElementById('editPitchModal'));
    myModal.show();
}

// HÀM MỞ MODAL XÓA SÂN
function deleteItem(name) {
    document.getElementById('deleteItemName').innerText = "Đang xóa: " + name;
    const myModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    myModal.show();
}
