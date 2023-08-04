import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import com.example.wallpapernotes.Note
import com.example.wallpapernotes.data.dataApi

class test1 {
    val context = InstrumentationRegistry.getInstrumentation().targetContext



    @Test
    fun testSaveAndRetrieveNoteList() {
        val x=dataApi(context)
        val note1 = Note("Title 1", "Description 1", "2023-08-03")
        val note2 = Note("Title 2", "Description 2", "2023-08-04")

        val noteList: MutableList<Note> = mutableListOf(note1, note2)
        x.updatedata(noteList)

        val retrievedNoteList = x.getdata()

        assertEquals(noteList, retrievedNoteList)
    }

}