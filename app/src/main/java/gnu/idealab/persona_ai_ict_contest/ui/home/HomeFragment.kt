package gnu.idealab.persona_ai_ict_contest.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.utils.loader.AiInfoMappingLoader
import gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view.CircularAdapter
import gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view.CircularLayoutManager
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentHomeBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var circularView: RecyclerView

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Log.d("HomeFragment", "onCreate called")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Log.d("HomeFragment", "onCreateView called")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
        // return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Log.d("HomeFragment", "onViewCreated called")

        // AiInfo 데이터 가져오기
        val aiInfoMappingLoader = AiInfoMappingLoader(requireContext())
        val aiInfoList = aiInfoMappingLoader.getAiList()

        // Log.d("HomeFragment", "aiInfoList를 다음과 같이 불러옴: $aiInfoList")

        // 리사이클러뷰 설정
        setupRecyclerView(aiInfoList)

        binding.circularRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var isScrollStopped = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 스크롤 시작 시 Shimmer 시작
                    Log.d("RecyclerViewScroll", "Shimmer 시작")
                    binding.shimmerSpeechText.visibility = View.VISIBLE
                    binding.shimmerSpeechText.startShimmer()
                    binding.speechText.visibility = View.INVISIBLE
                    isScrollStopped = false
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 스크롤 상태가 IDLE로 전환된 경우
                    isScrollStopped = true
                    Log.d("RecyclerViewScroll", "스크롤 정지 감지. 실제 멈춤 확인 중...")
                    recyclerView.postDelayed({
                        if (isScrollStopped) {
                            Log.d("RecyclerViewScroll", "Shimmer 멈춤 (1초 경과 후)")
                            binding.shimmerSpeechText.stopShimmer()
                            binding.shimmerSpeechText.visibility = View.GONE
                            binding.speechText.visibility = View.VISIBLE
                        }
                    }, 900) // 1초 딜레이
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // onScrolled 호출 시 스크롤이 아직 진행 중임을 알림
                if (dx != 0 || dy != 0) {
                    isScrollStopped = false
                }
            }
        })


        binding.infoDescriptionTextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_infoAppFragment)
        }

        binding.infoDescription2TextView.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_personaInfoFragment)
        }

    }

    private fun setupRecyclerView(aiInfoList: List<AiInfo>) {
        val circularAdapter = CircularAdapter(requireContext(), aiInfoList)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.circularRecyclerView.apply {
            this.layoutManager = layoutManager
            adapter = circularAdapter

            // 끝부분 여유 공간 추가
            val itemWidth = resources.getDimensionPixelSize(R.dimen.card_item_width)
            val sidePadding = (resources.displayMetrics.widthPixels - itemWidth) / 2
            setPadding(sidePadding, 0, sidePadding, 0)
            clipToPadding = false

            // RecyclerView가 초기화된 후 스크롤을 중앙으로 맞춤
            post {
                val centerPosition = aiInfoList.size / 2
                smoothScrollToPosition(centerPosition) // 중앙 아이템으로 이동
            }

            // 스냅 헬퍼 추가
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(this) // RecyclerView에 SnapHelper 연결
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}