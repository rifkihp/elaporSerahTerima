package elapor.application.com.model;

public class dataitem {
    String nama, satuan, keterangan;
    int jumlah;

    public dataitem(String nama, int jumlah, String satuan, String keterangan) {
        this.nama       = nama;
        this.jumlah     = jumlah;
        this.satuan     = satuan;
        this.keterangan = keterangan;
    }

    public String getNama() {
        return this.nama;
    }

    public int getJumlah() {
        return this.jumlah;
    }

    public String getSatuan() {
        return this.satuan;
    }

    public String getKeterangan() {
        return this.keterangan;
    }
}
