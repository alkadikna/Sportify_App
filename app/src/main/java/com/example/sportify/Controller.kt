package com.example.sportify

import androidx.compose.ui.input.key.Key.Companion.T
import com.example.sportify.Model.Field
import com.example.sportify.Model.Time
import com.google.android.gms.common.internal.Objects
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.concurrent.timer

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth

fun createField(newField: Field, path: String){
    val myRef = database.getReference(path).child(auth.uid!!).push()
    myRef.setValue(newField)
}

fun readField(){
    val myRef = database.getReference("field").child(auth.uid!!)
    myRef.get()
}

fun updateField(field: Field, path: String){
    val myRef = database.getReference("field").child(auth.uid!!).child(field.id!!)
    myRef.setValue(field)
}

fun deleteField(field: Field){
    val myRef = database.getReference("field").child(auth.uid!!).child(field.id!!)
    myRef.removeValue()
}

fun createTime(time: Time, path: String){
    val myRef = database.getReference("time").child(auth.uid!!).push()
    myRef.setValue(time)
}

fun readTime(){
    val myRef = database.getReference("time").child(auth.uid!!)
    myRef.get()
}

fun updateTime(time: Time, path: String){
    val myRef = database.getReference("time").child(auth.uid!!).child(time.id!!)
    myRef.setValue(time)}

fun deleteTime(time: Time){
    val myRef = database.getReference("time").child(auth.uid!!).child(time.id!!)
    myRef.removeValue()
}

fun create(newObject: Any, myRef: DatabaseReference){
    myRef.setValue(newObject)
}

fun read(myRef: DatabaseReference, onComplete: (List<Any>) -> Unit)  {
    myRef.get().addOnSuccessListener { dataSnapshot ->
        if(dataSnapshot.exists()){
           val timeList = mutableListOf<Any>()
            for(Snapshot in dataSnapshot.children){
                val time = dataSnapshot.getValue(Any::class.java)!!
                timeList.add(time)
            }
            onComplete(timeList)
        }
        else{
            onComplete(emptyList())
        }
    }
}

fun readById(myRef: DatabaseReference, unit: Unit){

}
fun delete(myRef: DatabaseReference){
    myRef.removeValue()
}

