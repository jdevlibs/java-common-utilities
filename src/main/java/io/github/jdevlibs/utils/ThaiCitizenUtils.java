package io.github.jdevlibs.utils;

import java.util.Random;

/**
 * @author supot.jdev
 * @version 1.0
 */
public final class ThaiCitizenUtils {
    private ThaiCitizenUtils(){

    }


    /**
     * Validate thai citizen id format
     * <pre>
     * หลักที่ 1 หมายถึงประเภทบุคคลซึ่งมี 8 ประเภท
     * หลักที่ 2 ถึงหลักที่ 5 หมายถึงรหัสของสำนักทะเบียนที่ท่านมีชื่อในทะเบียนบ้าน
     * โดยหลักที่ 2 และ 3 หมายถึงจังหวัด หลักที่ 4 และ 5 หมายถึงอำเภอ หรือเทศบาล
     * หลักที่ 6 ถึงหลักที่ 10 หมายถึงกลุ่มที่ของบุคคลแต่ละประเภทตามหลักแรก
     * หลักที่ 11 และ 12 หมายถึงลำดับที่ของบุคคลในแต่ละกลุ่มประเภท
     * หลักที่ 13 คือ ตัวเลขตรวจสอบความถูกต้องของเลข 12 หลักแรก
     *
     * สูตรการ Gen เลขบัตรประชาชน
     * 1. คูณตัวเลขในแต่ละหลัก
     *      byte1 * 13
     *      byte2 * 12
     *      byte3 * 11
     *      byte4 * 10
     *      byte5 * 9
     *      byte6 * 8
     *      byte7 * 7
     *      byte8 * 6
     *      byte9 * 5
     *      byte10 * 4
     *      byte11 * 3
     *      byte12 * 2
     * 2. รวมยอดทั้งหมดที่คูณกันในแต่ละหลัก
     * 3. นำผลลัพท์ข้อที่ 2 MOD 11
     * 4. นำผลลัพท์ข้อที่ 3 มา ลบ 11
     * 5. นำผลลัพท์ข้อที่ 4 มา Mod 10
     * 6. ถ้าผลลัพท์ตรงกับหลักสุดท้ายของเลขบัตร แสดงว่าบัตรถูกต้อง
     * </pre>
     * @param citizenId The thai citizen id
     * @return True when valid format
     */
    public static boolean isThaiCitizenId(String citizenId) {
        return Validators.isThaiCitizenId(citizenId);
    }

    public static boolean isNotThaiCitizenId(String citizenId) {
        return Validators.isNotThaiCitizenId(citizenId);
    }

    public static String generateThaiCitizenId() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int k = Math.abs(i + (-13));
            int m = rand.nextInt(9);
            if ((i == 0 || i == 1) && m == 0) {
                m = m + 1;
            }
            sb.append(m);
            sum += (k * m);
        }
        int checkSum = ((11 - (sum % 11)) % 10);
        sb.append(checkSum);
        return sb.toString();
    }
}
