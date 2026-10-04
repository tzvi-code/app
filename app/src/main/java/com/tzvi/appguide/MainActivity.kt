package com.tzvi.appguide

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

private data class Step(val title:String,val time:String,val summary:String,val details:List<String>,val url:String?=null,val label:String?=null,val prompt:Boolean=false)

private val prompt = """
אני רוצה לבנות אפליקציית Android אמיתית למתחילים, בלי ניסיון קודם בקוד.

עבוד ישירות במאגר GitHub שלי:
REPOSITORY_URL

דרישות:
1. צור בתוך המאגר פרויקט Android מלא ותקין ב-Kotlin וב-Jetpack Compose עם Material 3.
2. בנה ממשק מודרני, נקי ונוח למתחילים.
3. צור GitHub Actions שמבצע build ומפיק APK כ-artifact.
4. בדוק את הבנייה דרך GitHub Actions.
5. אם יש שגיאה, בדוק את הלוגים, תקן את הקוד ובצע ניסיון נוסף.
6. אל תסמן גמור עד שיש build ירוק ו-APK שנוצר בפועל.
7. כתוב README עם הוראות פתיחה, בנייה והתקנה.
8. אל תמציא תוצאות בדיקה שלא בוצעו.
""".trimIndent()

private val steps = listOf(
    Step("פותחים GitHub ויוצרים מאגר","דקה 1","יוצרים מאגר חדש בשם app.",listOf("התחבר ל-GitHub.","בחר Create a new repository.","קרא למאגר app.","מומלץ להתחיל ממאגר ריק."),"https://github.com/new","פתיחת GitHub"),
    Step("נכנסים ל-ChatGPT","דקה 2","פתח שיחה חדשה ב-ChatGPT.",listOf("התחבר לחשבון.","פתח שיחה חדשה.","בחר כלי/מודל שמתאים לעבודה עם קוד בהתאם למה שזמין אצלך.","הממשק ושמות המודלים עשויים להשתנות."),"https://chatgpt.com/","פתיחת ChatGPT"),
    Step("מחברים את GitHub","דקה 3","מחברים את GitHub ל-ChatGPT/Codex.",listOf("פתח את הגדרות Apps/Connectors לפי הממשק שמופיע אצלך.","חבר את GitHub ואשר את ההרשאות.","עבור עבודה שכותבת קוד למאגר השתמש בכלי עם הרשאת כתיבה, כגון Codex, אם זמין בחשבונך.","שים לב שחיבור GitHub רגיל יכול להיות לקריאה בלבד."),"https://help.openai.com/en/articles/11145903-connecting-github-to-chatgpt-deep-research-to-chatgpt-deep-research/","הוראות GitHub רשמיות של OpenAI"),
    Step("שולחים את הפרומט","דקה 4","מעתיקים את הפרומט, מחליפים את כתובת המאגר ושולחים.",listOf("לחץ על העתק פרומט מוכן.","החלף REPOSITORY_URL בכתובת המאגר שלך.","שלח את הפרומט ב-ChatGPT/Codex.","בקש לבדוק ולתקן GitHub Actions עד שה-build מצליח."),prompt=true),
    Step("מורידים APK ומתקינים","דקה 5","מורידים את ה-artifact ומתקינים אותו באנדרואיד.",listOf("פתח Actions במאגר.","פתח את הריצה שהסתיימה בהצלחה.","פתח Artifacts והורד app-debug-apk.","חלץ את ה-ZIP והתקן את ה-APK במכשיר."),"https://github.com/tzvi-code/app/actions","פתיחת Actions")
)

class MainActivity: ComponentActivity(){ override fun onCreate(b:Bundle?){super.onCreate(b);setContent{App()}} }

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun App(){
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl){
        val ctx=LocalContext.current
        val prefs=remember{ctx.getSharedPreferences("progress",Context.MODE_PRIVATE)}
        val done=remember{mutableStateListOf<Boolean>().apply{repeat(5){add(prefs.getBoolean("s$it",false))}}}
        var selected by remember{mutableStateOf(0)}
        val count=done.count{it}
        MaterialTheme(colorScheme=lightColorScheme(primary=Color(0xFF6750A4))){
            Scaffold(topBar={TopAppBar(title={Column{Text("בונים אפליקציה עם GPT",fontWeight=FontWeight.Bold);Text("מדריך למתחילים • $count/5",style=MaterialTheme.typography.labelMedium)}})},bottomBar={
                Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(10.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){
                    OutlinedButton(onClick={if(selected>0)selected--},enabled=selected>0,modifier=Modifier.weight(1f)){Icon(Icons.Default.ArrowForward,null);Text("הקודם")}
                    Button(onClick={if(selected<4)selected++},enabled=selected<4,modifier=Modifier.weight(1f)){Text(if(selected==4)"סיימנו" else "הבא");Icon(Icons.Default.ArrowBack,null)}
                }
            }){pad->LazyColumn(Modifier.fillMaxSize().padding(pad).padding(18.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
                item{ElevatedCard{Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){Text("מ-0 ל-APK",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text("חמישה צעדים פשוטים לבניית אפליקציה");LinearProgressIndicator(progress={count/5f},modifier=Modifier.fillMaxWidth());if(count>0)TextButton({done.indices.forEach{done[it]=false;prefs.edit().putBoolean("s$it",false).apply()}}){Icon(Icons.Default.Replay,null);Text("איפוס")}}}}
                item{Text("מסלול ההדרכה",style=MaterialTheme.typography.titleLarge,fontWeight=FontWeight.Bold)}
                items(steps.size){i->Card(onClick={selected=i},modifier=Modifier.fillMaxWidth(),colors=CardDefaults.cardColors(containerColor=if(i==selected)MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant)){Row(Modifier.padding(13.dp),verticalAlignment=Alignment.CenterVertically){Checkbox(checked=done[i],onCheckedChange={done[i]=it;prefs.edit().putBoolean("s$i",it).apply()});Column{Text("${i+1}. ${steps[i].title}",fontWeight=FontWeight.Bold);Text(steps[i].time)}}}}
                item{StepDetail(steps[selected],done[selected],{v->done[selected]=v;prefs.edit().putBoolean("s$selected",v).apply()})}
            }}
        }
    }
}

@Composable private fun StepDetail(step:Step,completed:Boolean,onDone:(Boolean)->Unit){
    val clip=LocalClipboardManager.current;val ctx=LocalContext.current
    ElevatedCard{Column(Modifier.padding(20.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
        Text(step.time,color=MaterialTheme.colorScheme.primary,fontWeight=FontWeight.Bold);Text(step.title,style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold);Text(step.summary)
        step.details.forEach{Row(verticalAlignment=Alignment.Top){Icon(Icons.Default.Check,null,Modifier.size(20.dp),tint=MaterialTheme.colorScheme.primary);Spacer(Modifier.width(8.dp));Text(it)}}
        if(step.url!=null)FilledTonalButton({ctx.startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(step.url)))},Modifier.fillMaxWidth()){Icon(Icons.Default.OpenInNew,null);Spacer(Modifier.width(6.dp));Text(step.label!!)}
        if(step.prompt)Card{Column(Modifier.padding(14.dp)){Text(prompt,maxLines=10,overflow=TextOverflow.Ellipsis,style=MaterialTheme.typography.bodySmall);Button({clip.setText(AnnotatedString(prompt));Toast.makeText(ctx,"הפרומט הועתק",Toast.LENGTH_SHORT).show()},Modifier.fillMaxWidth()){Icon(Icons.Default.ContentCopy,null);Spacer(Modifier.width(6.dp));Text("העתק פרומט מוכן")}}}
        Row(verticalAlignment=Alignment.CenterVertically){Checkbox(checked=completed,onCheckedChange=onDone);Text(if(completed)"הצעד הושלם" else "סמן כבוצע",fontWeight=FontWeight.SemiBold)}
    }}
}
