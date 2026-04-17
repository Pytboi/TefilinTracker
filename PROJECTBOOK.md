# ספר הפרויקט — TefilinTracker

> פרויקט גמר לבגרות במדעי המחשב — אפליקציית אנדרואיד ב-Java
> תיעוד זה נכתב בהתאם לדרישות ה"ספר הפרויקט" (Sefer Proyect).

---

## תוכן עניינים

1. [תכנון ותיעוד מסכי הפרויקט](#תכנון-ותיעוד-מסכי-הפרויקט)
2. [מימוש הפרויקט](#מימוש-הפרויקט)
3. [בסיס הנתונים](#בסיס-הנתונים)
4. [שימוש בקבצים](#שימוש-בקבצים)
5. [דרישות סילבוס בגרות — מיפוי](#דרישות-סילבוס-בגרות--מיפוי)

---

## תכנון ותיעוד מסכי הפרויקט

_(ירוכזו כאן כל המסכים — Activities ו-Fragments — עם טבלת רכיבי ה-UI שלהם.)_

---

## מימוש הפרויקט

_(כל מחלקת Java של האפליקציה — מודלים, Helpers, Activities — מתועדת בסעיף זה.)_

### `Gender` (enum)

- **שם המחלקה:** `com.example.myapplication.models.enums.Gender`
- **תפקיד המחלקה:** ייצוג מין המשתמש כערך מנוי (`MALE`, `FEMALE`). משמש כקלט לאלגוריתם סינון המצוות הרלוונטיות למשתמש.
- **Bagrut Requirement Fulfilled:** תכנות מונחה עצמים — Enum מטופס חזק.

**תכונות (Properties):** אין (Enum ללא שדות).

**פעולות (Methods):** אין (ערכי ה-Enum בלבד).

---

### `MaritalStatus` (enum)

- **שם המחלקה:** `com.example.myapplication.models.enums.MaritalStatus`
- **תפקיד המחלקה:** ייצוג מצב משפחתי של המשתמש (`SINGLE`, `MARRIED`). קלט נוסף לאלגוריתם סינון המצוות.
- **Bagrut Requirement Fulfilled:** תכנות מונחה עצמים — Enum מטופס חזק.

**תכונות (Properties):** אין.

**פעולות (Methods):** אין.

---

### `User`

- **שם המחלקה:** `com.example.myapplication.models.User`
- **תפקיד המחלקה:** מודל המייצג משתמש מאומת באפליקציה. מחזיק את נתוני הדמוגרפיה, ההעדפות הלשוניות, רשימת ההרגלים הפעילים ותאריך ההצטרפות. מחלקה זו עוברת סריאליזציה אוטומטית אל ומהמסמך `users/{uid}` ב-Firestore.
- **Bagrut Requirement Fulfilled:** תכנות מונחה עצמים — מחלקה עם שדות מרובים ושני אלגוריתמים לא טריוויאליים (`isHabitRelevantScope`, `addHabit`).

**תכונות (Properties):**

| שם השדה | סוג הנתון | תפקיד ושימוש |
|---|---|---|
| `MAX_ACTIVE_HABITS` | `int` (קבוע `static final`) | תקרה של 10 הרגלים פעילים — אכיפה של עקרון העידוד החיובי (לא להציף את המשתמש). |
| `uid` | `String` | מזהה ייחודי שמסופק ע"י Firebase Authentication. משמש כמפתח ראשי במסמך. |
| `displayName` | `String` | שם המשתמש כפי שיוצג ב-UI. |
| `email` | `String` | כתובת המייל ששימשה להרשמה. |
| `gender` | `Gender` | מין המשתמש — משפיע על סינון המצוות. |
| `maritalStatus` | `MaritalStatus` | מצב משפחתי — משפיע על סינון המצוות. |
| `preferredLanguage` | `String` | קוד שפה ISO: `en`, `iw`, `ru`, `fr`, `de`. |
| `notificationsEnabled` | `boolean` | האם התראות המינחה מופעלות. |
| `activeHabitIds` | `List<String>` | רשימת מזהי ההרגלים שהמשתמש בחר — שדה "זרות" (Foreign Keys) אל אוסף `habits_catalog`. |
| `createdAt` | `Date` | חותמת הזמן של יצירת המשתמש. |

**פעולות (Methods — לוגיקה משמעותית בלבד):**

| שם הפעולה | טיפוס מוחזר | הסבר אלגוריתמי |
|---|---|---|
| `User()` | — (בנאי) | בנאי ריק נדרש על-ידי Firestore לצורך Deserialization אוטומטי; מאתחל `activeHabitIds` לרשימה ריקה. |
| `User(uid, name, email, gender, marital, lang)` | — (בנאי) | בנאי מלא ליצירת משתמש חדש בסיום ה-Onboarding. קובע `notificationsEnabled=true` ו-`createdAt=now`. |
| `isHabitRelevantScope(String genderScope, String maritalScope)` | `boolean` | אלגוריתם לסינון מצוות: בודק שני פרדיקטים — האחד על מגדר (`ALL` / `MALE_ONLY` / `FEMALE_ONLY`) והשני על מצב משפחתי (`ALL` / `MARRIED_ONLY` / `SINGLE_ONLY`). מחזיר `true` רק אם שני הפרדיקטים מתקיימים. ערכים לא מוכרים נכשלים כברירת מחדל (Fail-closed). |
| `addHabit(String habitId)` | `boolean` | מוסיף מזהה הרגל לרשימה הפעילה, בכפוף לשני אילוצים: (1) אין כפילויות; (2) סך ההרגלים לא יעבור את `MAX_ACTIVE_HABITS`. אם אחד האילוצים נכשל, הרשימה אינה משתנה וההחזרה היא `false`. כולל אתחול עצל של הרשימה למקרה שהגיעה כ-`null` מ-Firestore. |
| `removeHabit(String habitId)` | `boolean` | מסיר הרגל מהרשימה; מחזיר `true` אם ההסרה בוצעה, `false` אם ההרגל לא היה ברשימה. |

---

## בסיס הנתונים

_(תרשים ה-ERD, האוספים ב-Firestore ושימושי ה-SharedPreferences יתועדו בסעיף זה.)_

---

## שימוש בקבצים

_(שימושי ה-`SharedPreferences` ושל קבצים מקומיים נוספים יתועדו בסעיף זה.)_

---

## דרישות סילבוס בגרות — מיפוי

| סעיף בסילבוס | דרישה | מקום מימוש באפליקציה | סטטוס |
|---|---|---|---|
| §6 | RecyclerView | `DashboardActivity` | 🟡 בתכנון |
| §6 | ViewPager + Fragments | `OnboardingActivity` | 🟡 בתכנון |
| §7 | Firestore (Remote DB) | `FirebaseRepository` | 🟡 בתכנון |
| §9 | GoogleMaps + Location | `MinyanRadarActivity` | 🟡 בתכנון |
| §9 | Multi-user Real-Time | `ChavrutaActivity` | 🟡 בתכנון |
| §10 | Calendar filter (Shabbat) | `Habit.isActiveOnDate()` | 🟡 בתכנון |
| §10 | SharedPreferences | `PrefsManager` | 🟡 בתכנון |
| §10 | AlarmManager + Notification | `NotificationScheduler` + `ReminderReceiver` | 🟡 בתכנון |
| OOP | מחלקה עם לוגיקה מורכבת | `User` (✅ `isHabitRelevantScope`, `addHabit`) | 🟢 מומש חלקית |
