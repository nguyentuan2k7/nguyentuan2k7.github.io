// --- 1. Cấu hình thông tin tài khoản MB của Anh Tuấn ---
const MY_BANK = {
    BANK_ID: "MB",               // Ngân hàng Quân đội
    ACCOUNT_NO: "0818841032",    // Số tài khoản của ông
    ACCOUNT_NAME: "NGUYEN ANH TUAN", // Tên viết hoa không dấu
    TEMPLATE: "qr_only"          // Chỉ lấy mã QR, không lấy khung rườm rà
};

// --- 2. Hàm tạo QR tự động theo số tiền và nội dung ---
function generateAutoQR(amount, description) {
    // Chuyển đổi nội dung sang dạng URL để API đọc được (xử lý dấu cách, ký tự đặc biệt)
    const encodedDesc = encodeURIComponent(description);
    const encodedName = encodeURIComponent(MY_BANK.ACCOUNT_NAME);

    // Tạo link API VietQR theo chuẩn Napas
    const qrUrl = `https://img.vietqr.io/image/${MY_BANK.BANK_ID}-${MY_BANK.ACCOUNT_NO}-${MY_BANK.TEMPLATE}.png?amount=${amount}&addInfo=${encodedDesc}&accountName=${encodedName}`;

    // Chèn ảnh QR vào khung có sẵn trên giao diện
    const qrContainer = document.querySelector('.qr-box');
    if (qrContainer) {
        qrContainer.innerHTML = `<img src="${qrUrl}" alt="Mã QR Thanh Toán" style="width:100%; max-width:250px; height:auto; border-radius:8px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">`;
    }
}

// --- 3. Xử lý đồng hồ đếm ngược ---
function initTimer(duration, elementId) {
    let timer = duration, minutes, seconds;
    const display = document.getElementById(elementId);

    const interval = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        if (display) display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(interval);
            alert("Đã hết thời gian thanh toán!");
        }
    }, 1000);
}

// --- 4. Chuyển sang trang VNPAY và tạo mã QR thực ---
function showVNPAY() {
    const checkoutPage = document.getElementById('checkout-page');
    const vnpayPage = document.getElementById('vnpay-page');

    // 1. Ẩn trang đặt vé, hiện trang QR
    checkoutPage.classList.add('hidden');
    vnpayPage.classList.remove('hidden');
    
    // 2. Đổi màu nền cho chuyên nghiệp
    document.body.style.backgroundColor = "#f2f2f7";

    // 3. Lấy số tiền cần thanh toán (bỏ dấu chấm và chữ đ)
    const rawAmount = "100000000"; // Hoặc lấy từ giao diện: document.querySelector('.final-price').innerText.replace(/\D/g, "");
    const desc = "Thanh toan ve Quoc Thien";

    // 4. Gọi hàm tạo mã QR dẫn về tài khoản của ông
    generateAutoQR(rawAmount, desc);

    // 5. Chạy đồng hồ đếm ngược 14 phút (Ảnh 3)
    initTimer(14 * 60, "qr-timer");
}

// --- 5. Đóng/Mở Modal Voucher ---
function toggleModal(show) {
    const modal = document.getElementById('voucher-modal');
    if (modal) {
        modal.classList.toggle('hidden', !show);
    }
}

// Khởi tạo đồng hồ 15 phút trang chính khi vừa mở web
window.onload = function () {
    initTimer(15 * 60, "countdown-clock");
};