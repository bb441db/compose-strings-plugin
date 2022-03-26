package github.bb441db

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import github.bb441db.ui.theme.ComposestringspluginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposestringspluginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(Strings.Main.world)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = Strings.Main.hello(p1 = name))
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposestringspluginTheme {
        Greeting(Strings.Main.world)
    }
}