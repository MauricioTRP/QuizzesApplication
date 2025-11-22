package com.example.quizzesapplication.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.quizzesapplication.R

val EyeOpen : ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.eye_opened)

val EyeClosed : ImageVector
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.eye_closed)

