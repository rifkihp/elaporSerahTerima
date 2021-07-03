package elapor.application.com.model;

import java.io.Serializable;

/**
 * Created by apple on 21/05/16.
 */
public class serahterima implements Serializable {

    private static final long serialVersionUID = 1L;

    int id;
    String tanggaljam;

    //YANG MENYERAHKAN:
    String piket_dari;  // pagi/siang/malam : (radio button)
    String regu_dari;  //(A)(B)(C)(D)      : (radio button)

    //YANG MENERIMA:
    String piket_kepada; // pagi/siang/malam : (radio button)
    String regu_kepada;  //(A)(B)(C)(D)      : (radio button)

    //KEKUATAN PENGAMANAN:
    int rupam;
    int p2u;
    int kplp;
    int perawat;
    int satops1;
    int satops2;

    String ket_rupam;
    String ket_p2u;
    String ket_kplp;
    String ket_perawat;
    String ket_satops1;
    String ket_satops2;

    int narapidana;
    int tahanan;
    int pertanian_ngajum;
    int rssa;
    int rsud;

    String foto;
    String keterangan;

    String file;

    public serahterima(
            int id,
            String tanggaljam,

            String piket_dari,
            String regu_dari,

            String piket_kepada,
            String regu_kepada,

            int rupam,
            int p2u,
            int kplp,
            int perawat,
            int satops1,
            int satops2,

            String ket_rupam,
            String ket_p2u,
            String ket_kplp,
            String ket_perawat,
            String ket_satops1,
            String ket_satops2,

            int narapidana,
            int tahanan,
            int pertanian_ngajum,
            int rssa,
            int rsud,
            
            String foto,
            String keterangan
    ) {
        this.id = id;
        this.tanggaljam = tanggaljam;
        
        this.piket_dari = piket_dari;
        this.regu_dari = regu_dari;
        
        this.piket_kepada = piket_kepada;
        this.regu_kepada = regu_kepada;

        this.rupam = rupam;
        this.p2u = p2u;
        this.kplp = kplp;
        this.perawat = perawat;
        this.satops1 = satops1;
        this.satops2 = satops2;

        this.ket_rupam = ket_rupam;
        this.ket_p2u = ket_p2u;
        this.ket_kplp = ket_kplp;
        this.ket_perawat = ket_perawat;
        this.ket_satops1 = ket_satops1;
        this.ket_satops2 = ket_satops2;

        this.narapidana = narapidana;
        this.tahanan = tahanan;
        this.pertanian_ngajum = pertanian_ngajum;
        this.rssa = rssa;
        this.rsud = rsud;

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

    public String getPiket_dari() {
        return this.piket_dari;
    }

    public String getRegu_dari() {
        return this.regu_dari;
    }

    public String getPiket_kepada() {
        return this.piket_kepada;
    }

    public String getRegu_kepada() {
        return this.regu_kepada;
    }

    public int getRupam() {
        return this.rupam;
    }
    
    public int getP2u() {
        return this.p2u;
    }
    
    public int getKplp() {
        return this.kplp;
    }

    public int getPerawat() {
        return this.perawat;
    }
    
    public int getSatops1() {
        return this.satops1;
    }

    public int getSatops2() {
        return this.satops2;
    }

    public String getKet_rupam() {
        return this.ket_rupam;
    }
    
    public String getKet_p2u() {
        return this.ket_p2u;
    }
    
    public String getKet_kplp() {
        return this.ket_kplp;
    }

    public String getKet_perawat() {
        return this.ket_perawat;
    }

    public String getKet_satops1() {
        return this.ket_satops1;
    }

    public String getKet_satops2() {
        return this.ket_satops2;
    }

    public int getNarapidana() {
        return this.narapidana;
    }

    public int getTahanan() {
        return this.tahanan;
    }

    public int getPertanian_ngajum() {
        return this.pertanian_ngajum;
    }

    public int getRssa() {
        return this.rssa;
    }

    public int getRsud() {
        return this.rsud;
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


