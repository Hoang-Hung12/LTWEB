
// Hàm mở Modal
function openModal() {
    document.getElementById('detailModal').style.display = 'flex';
}

// Hàm đóng Modal
function closeModal() {
    document.getElementById('detailModal').style.display = 'none';
}

// Chức năng: Đóng modal khi người dùng bấm ra ngoài khoảng tối (overlay)
window.onclick = function(event) {
    var modal = document.getElementById('detailModal');
    if (event.target == modal) {
        closeModal();
    }
}