package com.example.fauzighozali.nutapos.model

class Rekening {
    var rekeningID: Int? = null
    var namaBank: String? = null
    var nomorRekening: Int? = null
    var atasNama: String? = null

    constructor(rekeningID: Int?, namaBank: String?, nomorRekening: Int?, atasNama: String?) {
        this.rekeningID = rekeningID
        this.namaBank = namaBank
        this.nomorRekening = nomorRekening
        this.atasNama = atasNama
    }
}