
  const SLOTS = [
    {bd:'06:00',kt:'07:30'},{bd:'07:30',kt:'09:00'},{bd:'09:00',kt:'10:30'},
    {bd:'10:30',kt:'12:00'},{bd:'13:00',kt:'14:30'},{bd:'14:30',kt:'16:00'},
    {bd:'16:00',kt:'17:30'},{bd:'17:30',kt:'19:00'},{bd:'19:00',kt:'20:30'},
    {bd:'20:30',kt:'22:00'}
  ];

  let sanInfo      = null;
  let selectedSlot = null;
  let promoInfo    = null;
  let basePrice    = 0;
  let finalPrice   = 0;
  let selectedPayment = '';
  let proofImageBase64 = '';
  /** Fallback nếu ảnh VietQR không tải được */
  const QR_FALLBACK = 'https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=ARENAX%20MB%200965775144';

  const VIETQR = {
    bankId: 'MB',
    accountNo: '0965775144',
    accountName: 'HOANG XUAN HUNG',
    template: 'compact2'
  };

  function buildVietQrImageUrl(amountVnd, addInfo) {
    const amt = Math.max(0, Math.round(Number(amountVnd) || 0));
    const info = (addInfo || 'ARENAX DAT SAN').replace(/\s+/g, ' ').trim().slice(0, 80);
    const base = `https://img.vietqr.io/image/${VIETQR.bankId}-${VIETQR.accountNo}-${VIETQR.template}.jpg`;
    const q = new URLSearchParams();
    if (amt > 0) q.set('amount', String(amt));
    q.set('addInfo', info);
    q.set('accountName', VIETQR.accountName);
    return `${base}?${q.toString()}`;
  }

  function buildTransferContent() {
    const maSan = sessionStorage.getItem('arenax_maSan') || '';
    const ngay = (document.getElementById('pick-ngay') && document.getElementById('pick-ngay').value) || '';
    const compact = ngay.replace(/-/g, '');
    return `ARENAX ${maSan} ${compact}`.trim().slice(0, 80);
  }

  document.addEventListener('DOMContentLoaded', async () => {
    const user = getCurrentUser();
    if (!user) { window.location.href = 'login.html'; return; }

    const maSan = sessionStorage.getItem('arenax_maSan');
    if (!maSan) { showNotice('Không có thông tin sân để đặt!', 'danger'); history.back(); return; }

    document.getElementById('pick-ngay').value = new Date().toISOString().split('T')[0];
    document.getElementById('pick-ngay').min   = new Date().toISOString().split('T')[0];

    // Hiển thị điểm user
    if (user.diemTichLuy > 0) {
      document.getElementById('points-box').style.display = 'flex';
      document.getElementById('user-points-display').textContent = user.diemTichLuy;
    }

    try {
      const res = await fetch(API_BASE + '/api/san/' + maSan);
      if (!res.ok) throw new Error();
      sanInfo   = await res.json();
      basePrice = sanInfo.giaThue || 0;

      const img = sanInfo.anhChinh || 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=400&q=80';
      document.getElementById('bill-img').src = img;
      document.getElementById('bill-san-name').textContent = sanInfo.tenSan;
      document.getElementById('bill-san-loai').textContent = sanInfo.tenLoaiSan || '';
      document.getElementById('bill-gia').textContent = formatCurrency(basePrice);
      document.getElementById('loading-info').style.display = 'none';
      document.getElementById('main-content').style.display = 'block';

      await loadGioTrong();
      await loadPromotions();
      document.getElementById('bank-qr-img').addEventListener('error', function vietQrFallback() {
        if (this.dataset.fallbackApplied === '1') return;
        this.dataset.fallbackApplied = '1';
        this.src = QR_FALLBACK;
      });
      recalcTotal();
    } catch {
      document.getElementById('loading-info').innerHTML = '<p class="text-danger">Không tải được thông tin sân!</p>';
    }
  });

  async function loadGioTrong() {
    const ngay  = document.getElementById('pick-ngay').value;
    if (!ngay || !sanInfo) return;
    const maSan = sessionStorage.getItem('arenax_maSan');
    const wrap  = document.getElementById('time-slots-wrap');
    wrap.innerHTML = '<p class="text-muted">Đang kiểm tra giờ trống...</p>';
    document.getElementById('bill-ngay').textContent = formatDate(ngay);

    let booked = [];
    try {
      const res = await fetch(API_BASE + '/api/dondatsan/gio-da-dat/' + maSan + '?ngayDa=' + ngay);
      if (res.ok) booked = await res.json();
    } catch {}

    selectedSlot = null;
    document.getElementById('bill-gio').textContent = '—';
    validateSubmitBtn();

    wrap.innerHTML = SLOTS.map((sl, i) => {
      const isDone = booked.some(b => {
        return sl.bd < b.gioKetThuc && sl.kt > b.gioBatDau;
      });
      return `<div class="slot${isDone ? ' booked' : ''}" id="slot-${i}" onclick="${isDone ? '' : 'selectSlot('+i+')'}">
        ${sl.bd} - ${sl.kt}<br><small>${isDone ? 'Đã đặt' : 'Trống'}</small>
      </div>`;
    }).join('');
  }

  async function loadPromotions() {
    try {
      const res = await fetch(API_BASE + '/api/khuyenmai');
      if (!res.ok) return;
      const list = await res.json();
      const active = list.filter(k => k.trangThai === 1);
      if (!active.length) return;
      document.getElementById('promo-list').innerHTML = '<div><strong>Mã đang áp dụng:</strong> ' +
        active.map(k => `<span class="badge bg-light text-dark border me-1">${k.maCode}</span>`).join('') + '</div>';
    } catch (_) {}
  }

  function selectSlot(idx) {
    document.querySelectorAll('.slot').forEach(el => el.classList.remove('selected'));
    document.getElementById('slot-' + idx).classList.add('selected');
    selectedSlot = SLOTS[idx];
    document.getElementById('bill-gio').textContent = selectedSlot.bd + ' - ' + selectedSlot.kt;
    validateSubmitBtn();
    recalcTotal();
  }

  function selectPayment(method) {
    selectedPayment = method;
    document.getElementById('pay-cash').classList.toggle('active', method === 'TIEN_MAT');
    document.getElementById('pay-bank').classList.toggle('active', method === 'CHUYEN_KHOAN');
    const lbl = document.getElementById('confirm-deposit-label');
    const btn = document.getElementById('btn-submit');
    if (method === 'TIEN_MAT') {
      lbl.textContent = 'Tôi đã chuyển khoản đủ số tiền cọc (30%) theo QR và nội dung chuyển khoản.';
      btn.innerHTML = '<i class="fas fa-wallet me-2"></i>ĐÃ CHUYỂN KHOẢN CỌC &amp; ĐẶT SÂN';
    } else if (method === 'CHUYEN_KHOAN') {
      lbl.textContent = 'Tôi đã chuyển khoản đủ tổng tiền đơn theo QR và nội dung chuyển khoản.';
      btn.innerHTML = '<i class="fas fa-building-columns me-2"></i>CHUYỂN KHOẢN TOÀN BỘ &amp; ĐẶT SÂN';
    }
    updateQrInfo();
    validateSubmitBtn();
  }

  function validateSubmitBtn() {
    const ok = !!selectedSlot
      && !!selectedPayment
      && document.getElementById('confirm-deposit').checked
      && !!proofImageBase64;
    document.getElementById('btn-submit').disabled = !ok;
  }

  function handleProofFile(event) {
    const file = event.target.files && event.target.files[0];
    if (!file) {
      proofImageBase64 = '';
      document.getElementById('proof-preview-wrap').style.display = 'none';
      validateSubmitBtn();
      return;
    }
    if (!file.type.startsWith('image/')) {
      showNotice('Chỉ chấp nhận file ảnh chứng từ.', 'warning');
      event.target.value = '';
      proofImageBase64 = '';
      validateSubmitBtn();
      return;
    }
    const reader = new FileReader();
    reader.onload = () => {
      proofImageBase64 = String(reader.result || '');
      document.getElementById('proof-preview').src = proofImageBase64;
      document.getElementById('proof-preview-wrap').style.display = 'block';
      validateSubmitBtn();
    };
    reader.readAsDataURL(file);
  }

  async function applyPromo() {
    const code = document.getElementById('promo-input').value.trim();
    const msg  = document.getElementById('promo-msg');
    if (!code) { msg.innerHTML = '<span class="text-danger">Vui lòng nhập mã!</span>'; return; }
    try {
      const res = await fetch(API_BASE + '/api/khuyenmai/check/' + code);
      if (!res.ok) throw new Error();
      promoInfo = await res.json();
      const ruleText = getPromoRuleText(promoInfo.maCode);
      msg.innerHTML = '<span class="text-success"><i class="fas fa-check me-1"></i>Mã hợp lệ: ' + (promoInfo.tenKhuyenMai || code) + '</span>' +
        (ruleText ? `<div class="text-muted mt-1">${ruleText}</div>` : '');
      document.getElementById('promo-row').style.display = 'flex';
    } catch {
      promoInfo = null;
      msg.innerHTML = '<span class="text-danger"><i class="fas fa-times me-1"></i>Mã không hợp lệ hoặc đã hết hạn.</span>';
      document.getElementById('promo-row').style.display = 'none';
    }
    recalcTotal();
  }

  function getPromoRuleText(code) {
    const c = (code || '').toUpperCase();
    if (c.startsWith('VIP')) return 'Điều kiện: từ 1000 điểm tích lũy trở lên.';
    if (c.startsWith('VANG')) return 'Điều kiện: thành viên hạng Vàng.';
    if (c.startsWith('BAC')) return 'Điều kiện: từ 500 điểm tích lũy trở lên.';
    return '';
  }

  function recalcTotal() {
    let price = basePrice;
    let promoAmt = 0;
    if (promoInfo) {
      if ((promoInfo.loaiKhuyenMai || '').toLowerCase() === 'phantram') {
        promoAmt = Math.round(price * (promoInfo.giaTri / 100));
      } else {
        promoAmt = promoInfo.giaTri || 0;
      }
      price -= promoAmt;
      document.getElementById('bill-promo').textContent = '-' + formatCurrency(promoAmt);
    }
    const usePoints = document.getElementById('use-points').checked;
    let pointsAmt = 0;
    if (usePoints) {
      const user = getCurrentUser();
      const pts  = Math.floor((user.diemTichLuy || 0) / 100);
      pointsAmt  = pts * 10000;
      price      -= pointsAmt;
      document.getElementById('points-row').style.display = 'flex';
      document.getElementById('bill-diem').textContent = '-' + formatCurrency(pointsAmt);
    } else {
      document.getElementById('points-row').style.display = 'none';
    }
    price = Math.max(0, price);
    finalPrice = price;
    document.getElementById('bill-total').textContent = formatCurrency(price);
    document.getElementById('bill-coc').textContent   = formatCurrency(Math.round(price * 0.3));
    document.getElementById('bill-diem-thuong').textContent = Math.floor(price / 10000);
    updateQrInfo();
  }

  function updateQrInfo() {
    const coc = Math.round(finalPrice * 0.3);
    const isBank = selectedPayment === 'CHUYEN_KHOAN';
    const soTien = isBank ? finalPrice : coc;
    const guide = !selectedPayment
      ? 'Chọn Tiền mặt (cọc 30%) hoặc Chuyển khoản (toàn bộ) để hiện QR VietQR đúng số tiền.'
      : isBank
        ? 'Quét QR VietQR — số tiền = tổng đơn (thanh toán trước qua ngân hàng).'
        : 'Quét QR VietQR — số tiền = cọc 30% (bắt buộc trước khi đặt; phần còn lại trả tiền mặt tại sân).';
    document.getElementById('qr-guide').textContent = guide;
    document.getElementById('qr-amount').textContent = selectedPayment ? formatCurrency(soTien) : '—';
    const content = buildTransferContent();
    document.getElementById('qr-content').textContent = content || '—';
    const img = document.getElementById('bank-qr-img');
    if (!selectedPayment || finalPrice <= 0) {
      img.src = QR_FALLBACK;
      return;
    }
    img.src = buildVietQrImageUrl(soTien, content);
  }

  async function submitDat() {
    const user  = getCurrentUser();
    const maSan = sessionStorage.getItem('arenax_maSan');
    if (!selectedSlot) { showNotice('Vui lòng chọn khung giờ!', 'warning'); return; }
    if (!selectedPayment) { showNotice('Vui lòng chọn phương thức thanh toán!', 'warning'); return; }
    if (!document.getElementById('confirm-deposit').checked) {
      showNotice('Vui lòng tích xác nhận đã chuyển khoản đúng số tiền và nội dung.', 'warning');
      return;
    }
    if (!proofImageBase64) {
      showNotice('Vui lòng tải ảnh chứng từ chuyển khoản để gửi admin duyệt.', 'warning');
      return;
    }

    const ngay  = document.getElementById('pick-ngay').value;
    const dto   = {
      maKH: user.maTK, maSan,
      ngayDa: ngay,
      gioBatDau: selectedSlot.bd + ':00',
      gioKetThuc: selectedSlot.kt + ':00',
      diemSuDung: document.getElementById('use-points').checked
          ? Math.floor((user.diemTichLuy || 0) / 100) * 100 : 0,
      maCodeKhuyenMai: promoInfo ? promoInfo.maCode : null,
      phuongThucThanhToan: selectedPayment,
      daThanhToanCoc: true,
      chungTuThanhToan: proofImageBase64
    };

    const btn = document.getElementById('btn-submit');
    btn.disabled    = true;
      btn.innerHTML   = '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý thanh toán...';

    try {
      const res = await fetch(API_BASE + '/api/dondatsan', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dto)
      });
      if (!res.ok) {
        const err = await res.text();
        throw new Error(err);
      }
      const don = await res.json();
      showNotice(`Đã gửi yêu cầu đặt sân ${don.maDon}. Vui lòng chờ admin xác nhận thanh toán trước khi đơn được duyệt.`, 'success');
      setTimeout(() => { window.location.href = 'lich-su-dat-san.html'; }, 1200);
    } catch(e) {
      showNotice('Lỗi đặt sân: ' + (e.message || 'Vui lòng thử lại!'), 'danger');
      btn.disabled  = false;
      btn.innerHTML = selectedPayment === 'CHUYEN_KHOAN'
        ? '<i class="fas fa-building-columns me-2"></i>CHUYỂN KHOẢN TOÀN BỘ &amp; ĐẶT SÂN'
        : '<i class="fas fa-wallet me-2"></i>ĐÃ CHUYỂN KHOẢN CỌC &amp; ĐẶT SÂN';
    }
  }

  function showInvoice(don) {
    const user = getCurrentUser();
    document.getElementById('inv-ma-don').textContent   = don.maDon;
    document.getElementById('inv-san-name').textContent = don.tenSan || sanInfo.tenSan;
    document.getElementById('inv-loai').textContent     = sanInfo.tenLoaiSan || '';
    document.getElementById('inv-ngay').textContent     = formatDate(don.ngayDa);
    document.getElementById('inv-gio').textContent      = (don.gioBatDau||'').substring(0,5) + ' - ' + (don.gioKetThuc||'').substring(0,5);
    document.getElementById('inv-kh-name').textContent  = user.hoTen;
    document.getElementById('inv-kh-sdt').textContent   = user.sdt;
    document.getElementById('inv-total').textContent    = formatCurrency(don.tienSan);
    document.getElementById('inv-coc').textContent      = formatCurrency(don.tienCoc);
    document.getElementById('inv-diem').textContent     = (don.diemThuong || 0) + ' điểm';
    document.getElementById('invoiceModal').classList.add('show');

    // Điểm tích lũy sẽ được cộng khi hóa đơn hoàn tất ở phía admin.
  }

  function showNotice(msg, type = 'info') {
    document.getElementById('notice-wrap').innerHTML = `
      <div class="alert alert-${type} alert-dismissible fade show" role="alert">
        ${msg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    `;
  }

  function formatDate(d) {
    if (!d) return '—';
    const p = d.split('-');
    return p[2] + '/' + p[1] + '/' + p[0];
  }

