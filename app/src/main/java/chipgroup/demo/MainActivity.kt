package chipgroup.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import chipgroup.demo.ui.theme.ChipGroupTheme
import com.google.accompanist.flowlayout.FlowRow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent() {
    ChipGroupTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar( title = { Text(text = "Chip Group Demo")} )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TitleContent( "Multi Select Chip")
                MultiSelectChip()
                TitleContent( "Single Select Chip")
                SingleSelectChip()
            }
        }
    }
}

@Composable
fun MultiSelectChip() {

    // move to viewModel if you want to survive from configuration change
    val provinces  = remember {
        mutableStateListOf(
            ProvinceWithSelectState("AC", "Aceh"),
            ProvinceWithSelectState("RI", "Riau"),
            ProvinceWithSelectState("JA", "Jambi"),
            ProvinceWithSelectState("JK", "Jakarta"),
            ProvinceWithSelectState("JB", "Jawa Barat"),
            ProvinceWithSelectState("JT", "Jawa Tengah"),
            ProvinceWithSelectState("YO", "Yogyakarta"),
            ProvinceWithSelectState("JI", "Jawa Timur"),
            ProvinceWithSelectState("BA", "Bali"),
            ProvinceWithSelectState("NB", "Nusa Tenggara Barat")
        )
    }

    ChipContainer {
        provinces.forEachIndexed() { index, province ->
            ProvinceFilterChip(
                name = province.name,
                isSelected = province.isSelected,
                onClick = {
                    provinces[index] = province.copy(isSelected = !province.isSelected)
                }
            )
        }
    }

    val selectedProvinceCount = provinces.filter { it.isSelected }.size

    ChipInfoButton(
        text = "Selected province count : $selectedProvinceCount",
        isEnable = selectedProvinceCount > 0
    )
}

@Composable
fun TitleContent(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

@Composable
fun SingleSelectChip() {
    val provinces  = listOf(
        Province("AC", "Aceh"),
        Province("RI", "Riau"),
        Province("JA", "Jambi"),
        Province("JK", "Jakarta"),
        Province("JB", "Jawa Barat"),
        Province("JT", "Jawa Tengah"),
        Province("YO", "Yogyakarta"),
        Province("JI", "Jawa Timur"),
        Province("BA", "Bali"),
        Province("NB", "Nusa Tenggara Barat")
    )

    // using first province for default selection
    // move to viewModel if you want to survive from configuration change
    var selectedProvince by remember { mutableStateOf(provinces.first()) }

    ChipContainer {
        provinces.forEach { province ->
            ProvinceFilterChip(
                name = province.name,
                isSelected = province.code == selectedProvince.code,
                onClick =  {
                    selectedProvince = province
                }
            )
        }
    }

    ChipInfoButton(
        text = "Selected Province : ${selectedProvince.name}",
        isEnable = true
    )
}

@Composable
fun ChipInfoButton(
    text: String,
    isEnable: Boolean
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        onClick = { },
        enabled = isEnable,
    ) {
        Text(text = text)
    }
}

@Composable
fun ChipContainer(content: @Composable () -> Unit) {
    FlowRow(
        mainAxisSpacing = 8.dp,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvinceFilterChip(name: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        leadingIcon = {
            if (isSelected) {
                Icon(
                    modifier = Modifier.size(18.dp), //material design specs
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "$name checked"
                )
            }
        },
        selected = isSelected,
        label = { Text(name) },
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainActivityContent()
}

data class Province(
    val code: String,
    val name: String
)

data class ProvinceWithSelectState(
    val code: String,
    val name: String,
    var isSelected: Boolean = false
)