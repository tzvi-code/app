# בונים אפליקציה עם GPT

אפליקציית Android בעברית שמדריכה משתמשים ללא ניסיון בקוד לבנות אפליקציות בעזרת ChatGPT, GitHub ו-GitHub Actions.

## מה כולל הפרויקט
- 5 שלבי הדרכה עם ממשק RTL.
- Checklist שנשמר מקומית במכשיר.
- כפתור העתקת פרומט מוכן.
- קישורים ל-GitHub, ChatGPT ולהנחיות הרשמיות של OpenAI.
- Jetpack Compose + Material 3.
- GitHub Actions שבונה APK ומעלה אותו כ-artifact.

## Build
הפרויקט משתמש ב-Android Gradle Plugin 8.13.2, Kotlin 2.2.21, Gradle 8.13 ו-JDK 17.

ב-GitHub, כל push ל-main מפעיל את `.github/workflows/build-apk.yml`. לאחר build מוצלח אפשר להוריד את `app-debug-apk` מתוך Artifacts.

## חשוב לגבי GitHub ו-ChatGPT
חיבור GitHub ל-ChatGPT יכול לספק גישה לתוכן מאגרים. פעולות כתיבה/יצירת קבצים תלויות בכלי ובהרשאות הזמינים בחשבון, ולכן המדריך מציין שימוש ב-Codex או בכלי בעל הרשאת כתיבה כאשר נדרש לעדכן את המאגר.
