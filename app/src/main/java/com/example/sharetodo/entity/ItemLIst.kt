package com.example.sharetodo.entity

import java.io.Serializable


class ItemLIst : Serializable{

    var itemlist: String = ""
    var checklist: Boolean? = false

    constructor()

    fun setItem(itemList: String) {
        this.itemlist = itemList
    }

 /*   fun setCheckList(checkLista: Boolean) {
        this.checklist = checkLista
    }*/

    fun getItem(): String {
        return itemlist
    }

/*   fun getCheckList(): Boolean? {
        return checklist
    }*/

}
