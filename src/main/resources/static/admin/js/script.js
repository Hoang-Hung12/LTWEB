// file:// → origin "null" → ép về localhost (giống auth.js phía user)
function resolveAdminApiBase() {
    if (typeof window === 'undefined' || !window.location) return 'http://localhost:8080';
    const href = window.location.href || '';
    const origin = window.location.origin;
    if (!origin || origin === 'null' || href.startsWith('file:')) return 'http://localhost:8080';
    return origin;
}
const API = resolveAdminApiBase();

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
