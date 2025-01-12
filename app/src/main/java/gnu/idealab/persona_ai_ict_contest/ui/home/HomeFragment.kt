package gnu.idealab.persona_ai_ict_contest.ui.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import gnu.idealab.persona_ai_ict_contest.DefaultSetting
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.utils.loader.AiInfoMappingLoader
import gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view.CircularAdapter
import gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view.CircularLayoutManager
import gnu.idealab.persona_ai_ict_contest.custom_view.circular_scrolling_card_list_view.OnAiItemClickListener
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo
import gnu.idealab.persona_ai_ict_contest.data.models.ChatMessage
import gnu.idealab.persona_ai_ict_contest.data.repositories.ConnectRepository
import gnu.idealab.persona_ai_ict_contest.databinding.FragmentHomeBinding
import gnu.idealab.persona_ai_ict_contest.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.random.Random

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var circularView: RecyclerView

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var randomNumber = Random.nextInt(0, 6)
    private var isScrollStopped = false
    private val repos = ConnectRepository()

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

        binding.speechBubbleConstraint.setOnClickListener {
            if (isScrollStopped) {
                // 랜덤 숫자 생성
                val randomNumber = Random.nextInt(0, 6)

                // 중앙 아이템 가져오기
                val layoutManager = binding.circularRecyclerView.layoutManager as LinearLayoutManager
                val centerPosition = (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2

                val adapter = binding.circularRecyclerView.adapter as CircularAdapter
                val aiInfo = adapter.getItem(centerPosition)

                // 랜덤 텍스트 선택
                val descriptions = aiInfo.description.split("|")
                val randomDescription = descriptions[randomNumber]

                // 텍스트 업데이트
                binding.speechText.text = randomDescription

                // 텍스트 크기 애니메이션
                val scaleDownX = ObjectAnimator.ofFloat(binding.speechText, "scaleX", 1f, 0.9f)
                val scaleDownY = ObjectAnimator.ofFloat(binding.speechText, "scaleY", 1f, 0.9f)
                val scaleUpX = ObjectAnimator.ofFloat(binding.speechText, "scaleX", 0.9f, 1f)
                val scaleUpY = ObjectAnimator.ofFloat(binding.speechText, "scaleY", 0.9f, 1f)

                // 애니메이션 순서 정의
                val animatorSet = AnimatorSet().apply {
                    play(scaleDownX).with(scaleDownY) // 동시에 작아지고
                    play(scaleUpX).with(scaleUpY).after(scaleDownX) // 작아진 후 커진다
                    duration = 200 // 각 단계 300ms
                    interpolator = AccelerateDecelerateInterpolator() // 부드러운 가속/감속 효과
                }

                animatorSet.start() // 애니메이션 시작
            }
        }

        binding.circularRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    // 스크롤 시작 시 Shimmer 시작
                    Log.d("RecyclerViewScroll", "Shimmer 시작")
                    randomNumber = Random.nextInt(0, 6)
                    Log.d("RecyclerViewScroll", "randomNumber: $randomNumber")

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

                            // 중앙 아이템 가져오기
                            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                            val centerPosition = (layoutManager.findFirstVisibleItemPosition() + layoutManager.findLastVisibleItemPosition()) / 2

                            val adapter = recyclerView.adapter as CircularAdapter
                            val aiInfo = adapter.getItem(centerPosition)

                            // 랜덤 텍스트 선택
                            val descriptions = aiInfo.description.split("|")
                            val randomDescription = descriptions[randomNumber]

                            // 랜덤 텍스트 UI에 표시
                            binding.speechText.text = randomDescription

                            binding.shimmerSpeechText.stopShimmer()

                            binding.shimmerSpeechText.visibility = View.GONE

                            binding.speechText.visibility = View.VISIBLE

                            Log.d("RecyclerViewScroll", "중앙 아이템: $aiInfo")
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

    private fun gotoChatFragment(item: AiInfo, chatMessageList: List<ChatMessage>) {
        // Log.d("Test", chatMessageList.toTypedArray().toString())
        Log.d("GotoChatFragment", "Navigating with AiInfo: $item, Messages: $chatMessageList")

        val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(aiInfo = item, chatMessages = chatMessageList.toTypedArray())
        findNavController().navigate(action)
    }

    private fun setupRecyclerView(aiInfoList: List<AiInfo>) {
        // 아이템 클릭 시 채팅방 접근
        val circularAdapter = CircularAdapter(requireContext(), aiInfoList, object : OnAiItemClickListener {
            override fun onAiItemClick(item: AiInfo, isClicked: Boolean) {
                if (isClicked) {
                    val uid = DefaultSetting.getUID(requireContext())
                    viewModel.accessChatMessage(uid = uid, item = item)

                    // Lottie 애니메이션 시작
                    binding.skeletonLoadingLottieview.visibility = View.VISIBLE

                    // 최소 3초, 최대 30초 대기하며 결과 처리
                    viewLifecycleOwner.lifecycleScope.launch {
                        val result = withTimeoutOrNull(30000) { // 최대 30초 대기
                            repeatUntilSuccessOrTimeout()
                        }

                        // Lottie 애니메이션 멈추고 결과 처리
                        if (result == true) {
                            binding.skeletonLoadingLottieview.visibility = View.GONE
                            // Log.d("test", viewModel.chatHistoryList.value!!.toString())
                            gotoChatFragment(viewModel.aiInfo.value!!, viewModel.chatHistoryList.value!!)
                        } else {
                            binding.skeletonLoadingLottieview.visibility = View.GONE
                            // 실패 처리 (필요 시 에러 메시지 표시)
                        }
                    }
                }
            }
        })


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

    private suspend fun repeatUntilSuccessOrTimeout(): Boolean {
        val minDelay = 3000L // 최소 대기 시간: 3초
        val pollingInterval = 500L // 상태를 주기적으로 확인할 간격
        val startTime = System.currentTimeMillis()

        // 최소 3초가 지나기 전까지 무조건 대기
        delay(minDelay)

        while (System.currentTimeMillis() - startTime < 30000) { // 최대 30초 대기
            if (viewModel.connectSuccess.value == true) {
                return true // 성공 시 즉시 반환
            }
            delay(pollingInterval) // 폴링 간격만큼 대기
        }
        return false // 타임아웃
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}