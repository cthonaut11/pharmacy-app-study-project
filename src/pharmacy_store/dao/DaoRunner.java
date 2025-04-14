package pharmacy_store.dao;

import pharmacy_store.entity.Medicine;

import java.time.LocalDate;

public class DaoRunner {

    public static void main(String[] args) {
        saveMed();
//        MedicineDao medicineDao = MedicineDao.getInstance();
//        boolean deleteResult = medicineDao.delete(13);
//        System.out.println(deleteResult);
    }

    private static void saveMed() {
        MedicineDao medicineDao = MedicineDao.getInstance();
        Medicine medicine = new Medicine();

        medicine.setName("pivo");
        medicine.setCategory("UIO");
        medicine.setId_company_producer(1);
        medicine.setQuantity(30);
        medicine.setPurchase_price(30.5);
        medicine.setSelling_price(50.24);
        medicine.setExpiry_date(LocalDate.now());

        Medicine savedMedicine = medicineDao.save(medicine);
        System.out.println(savedMedicine);
    }
}
