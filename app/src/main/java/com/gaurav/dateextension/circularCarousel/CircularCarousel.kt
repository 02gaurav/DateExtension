package com.gaurav.dateextension.circularCarousel

import android.graphics.Movie
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularCarousel(data:List<MovieData>, modifier: Modifier) {
    val total = data.size
    val initialIndex = Int.MAX_VALUE / 2
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    LazyRow (state = listState){
        items(Int.MAX_VALUE) {index: Int ->
            MovieItem(data[index%total])
        }
    }
}

@Composable
fun MovieItem(data: MovieData){
    Box (modifier = Modifier.padding(16.dp).size(120.dp,220.dp).background(
        color = Color.Red,
        shape = RoundedCornerShape(12.dp)
    )){
        Text(data.name, modifier = Modifier.align(Alignment.Center))
    }
}

 data class MovieData(
    val id:Int,
    val name:String
)
