package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class pelanggaran implements Serializable {

    private static final long serialVersionUID = 1L;

    int id;
    String tanggaljam;

    //DATA PELANGGAR:
    String nama;
    String alamat;
    String pasal;
    String pidana;
    String blok;
    String jenis_pelanggaran;

    String foto;
    String keterangan;

    String file;

    public pelanggaran(
            int id,
            String tanggaljam,

            String nama,
            String alamat,
            String pasal,
            String pidana,
            String blok,
            String jenis_pelanggaran,
            
            String foto,
            String keterangan
    ) {
        this.id = id;
        this.tanggaljam = tanggaljam;

        this.nama = nama;
        this.alamat = alamat;
        this.pasal = pasal;
        this.pidana = pidana;
        this.blok = blok;
        this.jenis_pelanggaran = jenis_pelanggaran;

        this.foto = foto;
        this.keterangan = keterangan;

        this.file = null;
    }

    public int getId() {
        return this.id;
    }

    public String getTanggaljam() {
        return this.tanggaljam;
    }

    public String getNama() {
        return this.nama;
    }
    
    public String getAlamat() {
        return this.alamat;
    }
    
    public String getPasal() {
        return this.pasal;
    }

    public String getPidana() {
        return this.pidana;
    }

    public String getBlok() {
        return this.blok;
    }

    public String getJenis_pelanggaran() {
        return this.jenis_pelanggaran;
    }

    public String getFoto() {
        return this.foto;
    }

    public String getKeterangan() {
        return this.keterangan;
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}


