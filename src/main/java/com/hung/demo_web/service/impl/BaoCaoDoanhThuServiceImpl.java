package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.BaoCaoDoanhThuDto;
import com.hung.demo_web.dto.BaoCaoDoanhThuDto.*;
import com.hung.demo_web.entity.DonDatSan;
import com.hung.demo_web.entity.HoaDonEntity;
import com.hung.demo_web.repository.DonDatSanRepository;
import com.hung.demo_web.repository.HoaDonRepository;
import com.hung.demo_web.service.BaoCaoDoanhThuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BaoCaoDoanhThuServiceImpl implements BaoCaoDoanhThuService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private DonDatSanRepository donDatSanRepository;

    // -------------------------------------------------------
    // Lấy tất cả hóa đơn, sau đó lọc theo tháng/năm ở Java
    // (không cần @Query phức tạp, phù hợp project nhỏ)
    // -------------------------------------------------------
    private List<HoaDonEntity> locHoaDon(Integer thang, Integer nam) {
        return hoaDonRepository.findAll().stream()
                .filter(hd -> {
                    if (hd.getNgayLap() == null) return false;
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(hd.getNgayLap());
                    int thangHD = cal.get(Calendar.MONTH) + 1;
                    int namHD   = cal.get(Calendar.YEAR);

                    if (nam != null && namHD != nam)     return false;
                    if (thang != null && thangHD != thang) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // Lấy đơn hoàn thành, lọc theo tháng/năm của ngày đặt sân
    // -------------------------------------------------------
    private List<DonDatSan> locDonHoanThanh(Integer thang, Integer nam) {
        return donDatSanRepository.findAll().stream()
                .filter(don -> "Đã hoàn thành".equals(don.getTrangThai()))
                .filter(don -> {
                    if (don.getNgayDa() == null) return false;
                    if (nam   != null && don.getNgayDa().getYear()        != nam)   return false;
                    if (thang != null && don.getNgayDa().getMonthValue()  != thang) return false;
                    return true;
                })
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------
    // 1. TỔNG QUAN
    // -------------------------------------------------------
    @Override
    public BaoCaoDoanhThuDto tongQuan(Integer thang, Integer nam) {
        List<HoaDonEntity> hoaDons = locHoaDon(thang, nam);
        List<DonDatSan>    dons    = locDonHoanThanh(thang, nam);

        double tongDT  = hoaDons.stream().mapToDouble(hd -> hd.getTongThanhToan()  != null ? hd.getTongThanhToan()  : 0).sum();
        double tongDV  = hoaDons.stream().mapToDouble(hd -> hd.getTongTienDichVu() != null ? hd.getTongTienDichVu() : 0).sum();
        double tongSan = tongDT - tongDV;

        BaoCaoDoanhThuDto dto = new BaoCaoDoanhThuDto();
        dto.setTongDoanhThu(tongDT);
        dto.setTongTienSan(tongSan < 0 ? 0 : tongSan);
        dto.setTongTienDichVu(tongDV);
        dto.setSoHoaDon((long) hoaDons.size());
        dto.setSoDonHoanThanh((long) dons.size());
        dto.setThang(thang);
        dto.setNam(nam);
        return dto;
    }

    // -------------------------------------------------------
    // 2. DOANH THU THEO TỪNG THÁNG TRONG NĂM
    // -------------------------------------------------------
    @Override
    public BaoCaoDoanhThuDto theoThang(Integer nam) {
        int namLocTu = (nam != null) ? nam : Calendar.getInstance().get(Calendar.YEAR);

        List<HoaDonEntity> tatCa = locHoaDon(null, namLocTu);

        // Nhóm theo tháng
        Map<Integer, List<HoaDonEntity>> nhomTheoThang = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) nhomTheoThang.put(i, new ArrayList<>());

        for (HoaDonEntity hd : tatCa) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(hd.getNgayLap());
            int thangHD = cal.get(Calendar.MONTH) + 1;
            nhomTheoThang.get(thangHD).add(hd);
        }

        List<DoanhThuThangDto> dsThang = nhomTheoThang.entrySet().stream()
                .map(entry -> {
                    int    t     = entry.getKey();
                    List<HoaDonEntity> list = entry.getValue();
                    double dt    = list.stream().mapToDouble(hd -> hd.getTongThanhToan() != null ? hd.getTongThanhToan() : 0).sum();
                    return new DoanhThuThangDto(t, namLocTu, dt, (long) list.size());
                })
                .collect(Collectors.toList());

        BaoCaoDoanhThuDto dto = new BaoCaoDoanhThuDto();
        dto.setNam(namLocTu);
        dto.setDoanhThuTheoThang(dsThang);
        // Tổng năm
        double tongNam = dsThang.stream().mapToDouble(DoanhThuThangDto::getDoanhThu).sum();
        dto.setTongDoanhThu(tongNam);
        dto.setSoHoaDon((long) tatCa.size());
        return dto;
    }

    // -------------------------------------------------------
    // 3. DOANH THU THEO SÂN
    // -------------------------------------------------------
    @Override
    public BaoCaoDoanhThuDto theoSan(Integer thang, Integer nam) {
        List<HoaDonEntity> hoaDons = locHoaDon(thang, nam);

        // Nhóm hóa đơn theo mã sân
        Map<String, List<HoaDonEntity>> nhomTheoSan = new LinkedHashMap<>();
        Map<String, String> tenSanMap = new LinkedHashMap<>();

        for (HoaDonEntity hd : hoaDons) {
            if (hd.getDonDatSan() == null || hd.getDonDatSan().getSan() == null) continue;
            String maSan  = hd.getDonDatSan().getSan().getMaSan();
            String tenSan = hd.getDonDatSan().getSan().getTenSan();
            nhomTheoSan.computeIfAbsent(maSan, k -> new ArrayList<>()).add(hd);
            tenSanMap.put(maSan, tenSan);
        }

        List<DoanhThuSanDto> dsSan = nhomTheoSan.entrySet().stream()
                .map(entry -> {
                    String maSan   = entry.getKey();
                    List<HoaDonEntity> list = entry.getValue();
                    double doanhThu = list.stream()
                            .mapToDouble(hd -> hd.getTongThanhToan() != null ? hd.getTongThanhToan() : 0)
                            .sum();
                    return new DoanhThuSanDto(maSan, tenSanMap.get(maSan), doanhThu, (long) list.size());
                })
                // Sắp xếp doanh thu giảm dần
                .sorted(Comparator.comparingDouble(DoanhThuSanDto::getDoanhThu).reversed())
                .collect(Collectors.toList());

        BaoCaoDoanhThuDto dto = new BaoCaoDoanhThuDto();
        dto.setThang(thang);
        dto.setNam(nam);
        dto.setDoanhThuTheoSan(dsSan);
        double tongDT = dsSan.stream().mapToDouble(DoanhThuSanDto::getDoanhThu).sum();
        dto.setTongDoanhThu(tongDT);
        dto.setSoHoaDon((long) hoaDons.size());
        return dto;
    }

    // -------------------------------------------------------
    // 4. TOP KHÁCH HÀNG
    // -------------------------------------------------------
    @Override
    public BaoCaoDoanhThuDto topKhachHang(Integer thang, Integer nam, Integer topN) {
        List<HoaDonEntity> hoaDons = locHoaDon(thang, nam);
        int limit = (topN != null && topN > 0) ? topN : 10;

        // Nhóm theo mã khách hàng
        Map<String, Double>  tongChiTieu = new LinkedHashMap<>();
        Map<String, Long>    soLanDat   = new LinkedHashMap<>();
        Map<String, String>  hoTenMap   = new LinkedHashMap<>();
        Map<String, String>  sdtMap     = new LinkedHashMap<>();

        for (HoaDonEntity hd : hoaDons) {
            if (hd.getDonDatSan() == null || hd.getDonDatSan().getKhachHang() == null) continue;
            String maTK  = hd.getDonDatSan().getKhachHang().getMaTK();
            String hoTen = hd.getDonDatSan().getKhachHang().getHoTen();
            String sdt   = hd.getDonDatSan().getKhachHang().getSdt();
            double tien  = hd.getTongThanhToan() != null ? hd.getTongThanhToan() : 0;

            tongChiTieu.merge(maTK, tien, Double::sum);
            soLanDat.merge(maTK, 1L, Long::sum);
            hoTenMap.put(maTK, hoTen);
            sdtMap.put(maTK, sdt);
        }

        List<TopKhachHangDto> topList = tongChiTieu.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> new TopKhachHangDto(
                        entry.getKey(),
                        hoTenMap.get(entry.getKey()),
                        sdtMap.get(entry.getKey()),
                        entry.getValue(),
                        soLanDat.get(entry.getKey())
                ))
                .collect(Collectors.toList());

        BaoCaoDoanhThuDto dto = new BaoCaoDoanhThuDto();
        dto.setThang(thang);
        dto.setNam(nam);
        dto.setTopKhachHang(topList);
        return dto;
    }
}