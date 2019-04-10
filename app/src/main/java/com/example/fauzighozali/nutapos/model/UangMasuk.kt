package com.example.fauzighozali.nutapos.model

class UangMasuk {
    var uangMasukID: Int? = null
    var nomor: Int? = null
    var terimaDari: String? = null
    var keterangan: String? = null
    var jumlah: String? = null
    var tanggal: String? = null

    constructor(uangMasukID: Int?, nomor: Int?, terimaDari: String?, keterangan: String?, jumlah: String?, tanggal: String?) {
        this.uangMasukID = uangMasukID
        this.nomor = nomor
        this.terimaDari = terimaDari
        this.keterangan = keterangan
        this.jumlah = jumlah
        this.tanggal = tanggal
    }
}