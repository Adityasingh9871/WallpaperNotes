package com.example.wallpapernotes

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.rememberLazyNearestItemsRangeState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wallpapernotes.ui.theme.WallpaperNotesTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wallpapernotes.data.dataApi
import java.time.LocalDateTime
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateListOf

import androidx.compose.runtime.*
import com.example.wallpapernotes.ui.theme.WallpaperNotesTheme
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.Center
import java.time.LocalDate
import kotlin.reflect.KProperty

//import androidx.compose.runtime.livedata.observeAsState

data class Note(val id:String,val title:String,val desc:String,val date:String)
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context= LocalContext.current
            WallpaperNotesTheme {

                var disNotes  by remember { mutableStateOf(mutableListOf<Note>()) }

                val onAddNote: (String,String) -> Unit = {title,desc->
                    val id= LocalDateTime.now().toString()
                    val x=Note(id,title,desc, LocalDate.now().toString())
                    disNotes!!.add(x)
                    val dataApiInstance=dataApi()
                    dataApiInstance.updatedata(disNotes, context)
                    // Trigger re-composition
                    disNotes = disNotes.toMutableList()
                }

                val onDelete: (String) -> Unit = { id ->
                    disNotes = disNotes.filterNot { it.id == id }.toMutableList()
                    val dataApiInstance=dataApi()
                    dataApiInstance.updatedata(disNotes, context)
                    Log.d("delete ", "deleted")
                }

                    val dataApiInstance=dataApi()
                    disNotes= dataApiInstance.getdata(context)!!
                    AddButton(disNotes,onAddNote,onDelete)
//                    defaultPreview()
                }
            }
        }
    }


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun defaultPreview()
{
    var isAddDialog by remember{ mutableStateOf(false) }
    val onAddNote: (String,String) -> Unit = {title,desc->
        val id= LocalDateTime.now().toString()
        val x=Note(id,title,desc, LocalDate.now().toString())
//        disNotes!!.add(x)
        val dataApiInstance=dataApi()
//        dataApiInstance.updatedata(disNotes, context)
        // Trigger re-composition
//        disNotes = disNotes.toMutableList()
    }

    val onDelete: (String) -> Unit = { id ->
//        disNotes = disNotes.filterNot { it.id == id }.toMutableList()
        val dataApiInstance=dataApi()
//        dataApiInstance.updatedata(disNotes, context)
        Log.d("delete ", "deleted")
    }

//    DialogBox(onAddNote , OnClose = {isAddDialog=true}, context = LocalContext.current)
    NotesPerRow("a","a","a","date",onDelete)

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DialogBox(onAddNote: (String,String) -> Unit,OnClose:(Boolean)->Unit,context: Context)
{
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = { /*TODO*/ },
        confirmButton ={
                       Button(onClick = { onAddNote(title,desc);OnClose(false) }, modifier = Modifier.fillMaxWidth(1f)) {
                          Text(text = "Save")
                       }
        },
        shape=RoundedCornerShape(16.dp),
        containerColor = Color.White,
        title={
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = TopEnd)
            {
                IconButton(onClick = { OnClose(false) }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription ="", tint = Color.Red)
                }
            }

        },
        text={
            Column(modifier = Modifier.padding(horizontal = 10.dp), horizontalAlignment = Alignment.End) {
                AppTextField(text = title, onValueChange = { title=it }, placeholder = "Title", modifier = Modifier)
                Spacer(modifier = Modifier.height(10.dp))
                AppTextField(text = desc, onValueChange ={desc=it}, placeholder = "Description", modifier = Modifier.height(300.dp))
            }
        }
        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(text:String, onValueChange: (String) -> Unit,placeholder:String, modifier: Modifier)
{
    TextField(value = text, onValueChange =onValueChange,
        modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            textColor = Color.Black // Set the text color to black
        ),
        placeholder = { Text(text = placeholder,color=Color.Black.copy(alpha = .4f))}
    )
}

@Composable
fun NotesPerRow(
    id:String,
    title:String,
    desc: String,
    date: String,
    onDelete:(String)->Unit,
    modifier: Modifier=Modifier
)
{
    Box(modifier = Modifier
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        .padding(all = 2.dp)
        .fillMaxWidth(.9f)
        , contentAlignment = Center)
    {
        Column(modifier=Modifier.padding(all=10.dp)) {
            Row(modifier=Modifier.fillMaxWidth(1f)) {
                Text(text = title, style = TextStyle(
                    color=Color.Black,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                ),modifier=Modifier.weight(.7f)
                )
                IconButton(onClick = { onDelete(id) },modifier= Modifier
                    .weight(.3f)
                    .align(CenterVertically)) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "",tint = Color.Red.copy(alpha = .5f))
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = desc, style = TextStyle(
                color= Color.Black.copy(alpha = .7f),
                fontSize = 18.sp
            ))
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = date, style = TextStyle(
                color= Color.Black.copy(alpha = .3f),
                fontSize = 10.sp
            ))
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddButton(Notes:MutableList<Note>?,onAddNote: (String,String)->Unit,onDelete: (String) -> Unit)
{
    var search by remember{ mutableStateOf("") }
    var title by remember{ mutableStateOf("") }
    var desc by remember{ mutableStateOf("") }
    var isAddDialog by remember{ mutableStateOf(false) }
//    Notes.removeAll(listOf(null))

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { isAddDialog=true }) {
            Icon(imageVector = Icons.Default.Add, contentDescription ="", tint = Color.Black)
        }
    }, modifier = Modifier.padding(all=0.dp)) {paddingValues->

        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier
            .fillMaxSize(1f)
            .padding(paddingValues))
        {
            AppSearchBar(search = search, OnValueChange = {search=it},modifier=Modifier)
//            NotesPerRow(data="aditya", Delete = {})

            Column(verticalArrangement = Arrangement.spacedBy(8.dp) ,modifier = Modifier.verticalScroll(rememberScrollState())) {

                if (Notes!=null)
                    Notes.map { x->NotesPerRow(id=x.id,title =x.title,desc=x.desc,x.date,onDelete, modifier = Modifier) }

            }


            if(isAddDialog)
                DialogBox(
                    onAddNote,
                    OnClose = { isAddDialog = it },
                    LocalContext.current
                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(search:String,OnValueChange:(String)->Unit,modifier: Modifier=Modifier)
{
    TextField(value =search , onValueChange ={OnValueChange} , modifier = Modifier
        .fillMaxWidth(1f)
        .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp), shape = RoundedCornerShape(10.dp), colors = TextFieldDefaults.textFieldColors(
        containerColor = Color.Gray,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent
    ),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "",tint=Color.White)},
        trailingIcon = {
            if(search.isNotEmpty())
            {
                IconButton(onClick = {OnValueChange("")}) {
                    Icon(imageVector = Icons.Default.Close, contentDescription ="" )
                }
            }
        },
        placeholder = { Text(text = "Search Notes")}
    )
}
