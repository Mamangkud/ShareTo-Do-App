package Database

import com.example.sharetodo.entity.ItemLIst

data class MyTask(
    var author: String,
    var id: String?,
    var judul: String,
    var waktu: String,
    var listItem: ArrayList<ItemLIst>

){

    constructor():
            this("","","","", ArrayList()){
    }


}