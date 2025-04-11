package com.lra.kalanikethencic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lra.kalanikethencic.R

val plusJakartaFontFamily: FontFamily = FontFamily(
    Font(R.font.jakartasans_regular, FontWeight.Normal),
    Font(R.font.jakartasans_italic, FontWeight.Normal, FontStyle.Italic),

    Font(R.font.jakartasans_bold, FontWeight.Bold),
    Font(R.font.jakartasans_bolditalic, FontWeight.Bold, FontStyle.Italic),

    Font(R.font.jakartasans_semibold, FontWeight.SemiBold),
    Font(R.font.jakartasans_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),

    Font(R.font.jakartasans_extrabold, FontWeight.ExtraBold),
    Font(R.font.jakartasans_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),

    Font(R.font.jakartasans_light, FontWeight.Light),
    Font(R.font.jakartasans_lightitalic, FontWeight.Light, FontStyle.Italic),

    Font(R.font.jakartasans_extralight, FontWeight.ExtraLight),
    Font(R.font.jakartasans_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),

    Font(R.font.jakartasans_medium, FontWeight.Medium),
    Font(R.font.jakartasans_mediumitalic, FontWeight.Medium, FontStyle.Italic),
)



// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle( // basically only for Login and Payment Screen
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 40.sp,
        lineHeight = 56.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle( // The title on the Home screen, Classes Screen and Classes Edit Screen
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 35.sp,
        lineHeight = 49.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle( // For the App bar at the top
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 18.sp,
        lineHeight = 25.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle( // For the studentBox component, ParentBox Component, Student Info Card and class component
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle( // For the Class Student Component, Payment History Component and History Component
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    titleSmall = TextStyle( // For the Payment Component, button component, selection wrap component and content wrap component, also the title of the colunms in the studentbox and parentbox components.
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle( // For the sidebar
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle( // I cba this is basically everywhere just look for it
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),

    bodySmall = TextStyle( // Basically only on the payment component for The Id and amount.
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    ),

    labelMedium = TextStyle( // Basically only on the payment component For the view history button, also on the Classes composable for the edit class, button.
        fontFamily = plusJakartaFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp, // Recommended: ~1.4 * fontSize
        letterSpacing = 0.sp
    )
)