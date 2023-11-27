package com.example.mykanjilist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class KanjiItem(
    val id:Int,
    var kanji:String,
    var hiragana:String,
    var meaning:String,
    var isEditing:Boolean = false
)

@Composable
fun KanjiListApp(){

    var sKanjis by remember { mutableStateOf(listOf<KanjiItem>() ) }
    var showDialog by remember { mutableStateOf(false) }
    var kanjiName by remember { mutableStateOf("")  }
    var hiragenaName by remember { mutableStateOf("")  }
    var meaningName by remember { mutableStateOf("")  }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            showDialog = true
        },
            modifier = Modifier.align(Alignment.CenterHorizontally))
        {
            Icon(Icons.Default.AddCircle, contentDescription = "Add")
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "Add Kanji")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sKanjis){
                item ->
                if(item.isEditing){
                    KanjiItemEditor(kanji = item,
                        onEditComplete = {
                          editedKanji,
                          editedHiragana,
                          editedMeaning ->
                          sKanjis = sKanjis.map{it.copy(isEditing = false)}
                          val editedItem = sKanjis.find { it.id == item.id }
                            editedItem?.let{
                              it.kanji = editedKanji
                              it.hiragana = editedHiragana
                              it.meaning = editedMeaning
                          }
                    })
                }else{
                    KanjiListItem(kanji = item, onEditClick = {
                        sKanjis = sKanjis.map { it.copy(isEditing = it.id == item.id) }
                    }, onDeleteClick = {
                        sKanjis = sKanjis-item
                    })
                }
            }
        }


    }
    
    if(showDialog){
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Button(onClick = {
                                    if(kanjiName.isNotBlank()){
                                        val newKanji = KanjiItem(
                                            id = sKanjis.size+1,
                                            kanji =  kanjiName,
                                            hiragana = hiragenaName,
                                            meaning = meaningName
                                        )
                                        sKanjis = sKanjis + newKanji
                                        showDialog = false
                                        kanjiName = ""
                                        hiragenaName = ""
                                        meaningName = ""
                                    }
                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = {
                                    showDialog = false
                                }) {
                                    Text(text = "Cancel")
                                }
                            }
            },
            title = { Text(text = "Add Kanji")},
            text = {
                Column {
                    OutlinedTextField(
                        value = kanjiName,
                        onValueChange = {kanjiName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Enter kanji")})

                    OutlinedTextField(
                        value = hiragenaName,
                        onValueChange = {hiragenaName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Enter hirakana")})

                    OutlinedTextField(
                        value = meaningName,
                        onValueChange = {meaningName = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Enter meaning")})
                }
            })
    }
}

@Composable
fun KanjiItemEditor(
    kanji: KanjiItem,
    onEditComplete: (String, String, String) -> Unit){
        var editedKanji by remember { mutableStateOf(kanji.kanji)   }
        var editedHiragana by remember { mutableStateOf(kanji.hiragana)   }
        var editedMeaning by remember { mutableStateOf(kanji.meaning)   }
        var isEditing by remember { mutableStateOf(kanji.isEditing)   }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column {
                BasicTextField(
                    value = editedKanji,
                    onValueChange = {editedKanji = it},
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        ,
                    textStyle = TextStyle(
                        fontSize = 23.sp
                    ))
                BasicTextField(
                    value = editedHiragana,
                    onValueChange = {editedHiragana = it},
                    singleLine = true,
                    modifier = Modifier
                        .wrapContentSize()
                        )


            }
        Column {
            BasicTextField(
                value = editedMeaning,
                onValueChange = {editedMeaning = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize().padding(top = 10.dp),

                    )
        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedKanji, editedHiragana, editedMeaning)
        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun KanjiListItem(
    kanji : KanjiItem,
    onEditClick : () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0XFF018786)),
                shape = RoundedCornerShape(20)

            ),
       horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = kanji.kanji,
                modifier = Modifier.padding(start = 18.dp),
                style = TextStyle(
                    fontSize = 30.sp
                )
            )
            Text(text = kanji.hiragana, modifier = Modifier.padding( start = 18.dp, bottom = 15.dp,))
        }
        Column (verticalArrangement = Arrangement.Center){
            Text(text = kanji.meaning, modifier = Modifier.padding(8.dp),
                style = TextStyle(
                    fontSize = 30.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
                )
        }


        Column() {
            Row(modifier = Modifier.padding(8.dp),
                // horizontalArrangement = Arrangement.End,
            ){
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }

    }

}