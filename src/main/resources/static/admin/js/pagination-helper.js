/**
 * pagination-helper.js
 * Hàm renderPagination dùng chung — kiểu ảnh 7: < 1 2 3 >
 *
 * @param {string} containerId  - id của div chứa pagination
 * @param {number} currentPage  - trang hiện tại (1-based)
 * @param {number} totalPages   - tổng số trang
 * @param {Function} onPageChange - callback(newPage) khi click
 */
function renderPagination(containerId, currentPage, totalPages, onPageChange) {
  const wrap = document.getElementById(containerId);
  if (!wrap) return;

  if (totalPages <= 1) { wrap.innerHTML = ''; return; }

  // Kiểu: < 1 2 3 … n >
  const btn = (label, page, disabled, active, isIcon) => {
    const cls = [
      'btn btn-sm',
      active  ? 'btn-primary' : 'btn-outline-secondary',
      disabled ? 'disabled' : ''
    ].join(' ').trim();
    const content = isIcon ? `<i class="fas fa-chevron-${label}"></i>` : label;
    return `<button class="${cls}"
              style="min-width:34px"
              ${disabled ? 'disabled' : ''}
              onclick="${disabled ? '' : `(${onPageChange.toString()})(${page})`}">
              ${content}
            </button>`;
  };

  let html = btn('left', currentPage - 1, currentPage === 1, false, true);

  // Trang đầu luôn hiển thị
  html += btn(1, 1, false, currentPage === 1, false);

  if (totalPages <= 7) {
    // Ít trang: hiện tất cả
    for (let p = 2; p <= totalPages; p++) {
      html += btn(p, p, false, currentPage === p, false);
    }
  } else {
    // Nhiều trang: rút gọn bằng "…"
    const ellipsis = `<button class="btn btn-sm btn-outline-secondary disabled" style="min-width:34px" disabled>…</button>`;

    if (currentPage <= 4) {
      for (let p = 2; p <= Math.min(5, totalPages - 1); p++) {
        html += btn(p, p, false, currentPage === p, false);
      }
      html += ellipsis;
    } else if (currentPage >= totalPages - 3) {
      html += ellipsis;
      for (let p = totalPages - 4; p <= totalPages - 1; p++) {
        html += btn(p, p, false, currentPage === p, false);
      }
    } else {
      html += ellipsis;
      for (let p = currentPage - 1; p <= currentPage + 1; p++) {
        html += btn(p, p, false, currentPage === p, false);
      }
      html += ellipsis;
    }

    html += btn(totalPages, totalPages, false, currentPage === totalPages, false);
  }

  html += btn('right', currentPage + 1, currentPage === totalPages, false, true);
  wrap.innerHTML = html;
}
