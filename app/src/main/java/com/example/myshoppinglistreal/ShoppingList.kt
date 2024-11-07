package com.example.myshoppinglistreal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults.color
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
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.text.style.TextForegroundStyle.Unspecified.color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing:Boolean = false
)
@Composable
fun ShoppingListApp(){
    var sItem by remember{ mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember{ mutableStateOf(false) }
    var itemName by remember{ mutableStateOf("") }
    var itemQuantity by remember{ mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                      showDialog = true
                      },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(15.dp)

        ) {
            Text(text = "Add Item")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            items(sItem){
                item ->
                if(item.isEditing){
                    ShippingItemEditor(item = item, onEditComplete = {
                        editName, editQuantity->
                        sItem = sItem.map{it.copy(isEditing = false)}
                        var editedItem = sItem.find{it.id == item.id}
                        editedItem?.let {
                            it.name = editName
                            it.quantity = editQuantity
                        }
                    })
                }
                else{
                    ShoppingListItem(item = item,
                        onEditclick = {
                        // finding out which item we are editing and
                        // changing is"isEditing boolean "  to true
                        sItem = sItem.map { it.copy(isEditing = it.id == item.id) }
                    },
                        onDeleteClick = {
                        sItem = sItem-item
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
                        if(itemName.isNotBlank()){
                            val newItem = ShoppingItem(
                                id = sItem.size +1,
                                name = itemName,
                                quantity = itemQuantity.toInt()
                            )
                            sItem =  sItem + newItem
                            showDialog = false
                            itemName = ""
                            itemQuantity = ""
                        }
                    }) {
                        Text(text = "Add")
                    }
                    Button(onClick = { showDialog = false}) {
                        Text(text = "Cancel")
                        
                    }
                }
        },
            title = { Text(text = "Add Shipping Item")},
            text = {
                Column {
                    OutlinedTextField(
                        value = itemName,
                        onValueChange ={itemName = it},
                        singleLine = true,
                        label = { Text("Item Name") }, // Label acts as a hint
                        placeholder = { Text("Enter item name") }, // Hint text
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange ={itemQuantity = it},
                        singleLine = true,
                        label = { Text("Item Quantity") }, // Label acts as a hint
                        placeholder = { Text("Enter item Quantity") }, // Hint text
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }

            }
            )
    }
}

@Composable
fun ShippingItemEditor(item: ShoppingItem, onEditComplete:(String , Int) -> Unit){
     var editName by remember{ mutableStateOf(item.name) }
     var editQuantity by remember{ mutableStateOf(item.quantity.toString()) }
     var isEditing by remember{ mutableStateOf(item.isEditing) }
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.LightGray)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
        )
    {

            Column(
                modifier = Modifier
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp)) // Border with rounded corners
                    .padding(5.dp) // Padding inside the column
            ) {
                BasicTextField(
                    value = editName,
                    onValueChange = {editName = it},
                    singleLine = true,
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .padding(8.dp)
                )
            }

            Column (
                modifier = Modifier
                    .border(1.dp, Color.Black, shape = RoundedCornerShape(10.dp)) // Border with rounded corners
                    .padding(5.dp) // Padding inside the column
            ){
                BasicTextField(
                    value = editQuantity,
                    onValueChange = {editQuantity = it},
                    singleLine = true,
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .padding(8.dp)
                )
            }

        Button(onClick = {
            isEditing = false
            onEditComplete(editName , editQuantity.toIntOrNull()?:1)

        }) {
            Text(text = "Save")
        }
    }
}

@Composable
fun ShoppingListItem(
    item :ShoppingItem,
    onEditclick: ()->Unit,
    onDeleteClick: () ->Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF1030D6)),
                shape = RoundedCornerShape(10)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ){
        Text(text = item.name , modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}" , modifier =Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditclick){
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            IconButton(onClick = onDeleteClick){
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}
