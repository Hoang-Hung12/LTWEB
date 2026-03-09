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

// VẼ BIỂU ĐỒ DOANH THU (Chỉ chạy khi ở trang revenue.html)
document.addEventListener("DOMContentLoaded", function() {
    const canvasElement = document.getElementById('revenueChart');
    
    // Nếu trang hiện tại có thẻ canvas biểu đồ thì mới vẽ, tránh lỗi JS
    if (canvasElement) {
        const ctx = canvasElement.getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['06/02', '07/02', '08/02', '09/02', '10/02', '11/02', '12/02'],
                datasets: [{
                    label: 'Doanh thu (VNĐ)',
                    data: [1500000, 2200000, 1800000, 2000000, 3500000, 2200000, 2750000],
                    borderColor: '#0d6efd',
                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.4
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        grid: { borderDash: [5, 5] }
                    },
                    x: { grid: { display: false } }
                }
            }
        });
    }
});