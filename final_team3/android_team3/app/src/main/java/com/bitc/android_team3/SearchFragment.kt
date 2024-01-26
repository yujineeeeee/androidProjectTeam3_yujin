import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.bitc.android_team3.CategoryAdapter
//import com.bitc.android_team3.CategoryData
import com.bitc.android_team3.DetailActivity
import com.bitc.android_team3.MainActivity
import com.bitc.android_team3.databinding.ActivityDetailBinding
import com.bitc.android_team3.databinding.FragmentSearchBinding
import com.bitc.android_team3.databinding.RecyclerItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSearch.setOnClickListener {
            val name = binding.editTextSearch.text.toString()
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }

        binding.categoPre.setOnClickListener {
            val intent = Intent(requireContext(), HomeFragment::class.java)
            startActivity(intent)
        }
    }
}
