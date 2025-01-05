package gnu.idealab.persona_ai_ict_contest.utils.loader

import android.content.Context
import android.content.res.XmlResourceParser
import gnu.idealab.persona_ai_ict_contest.R
import gnu.idealab.persona_ai_ict_contest.data.models.AiInfo

class AiInfoMappingLoader(private val context: Context) {
    
    private var basePath = "" // 이미지 기본 경로
    private val mapping = mutableMapOf<String, AiInfo>()

    init {
        loadMapping()
    }

    private fun loadMapping() {
        val parser: XmlResourceParser = context.resources.getXml(R.xml.ai_profile_setting)
        parser.use {
            var eventType = parser.eventType
            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    when (parser.name) {
                        "ai_profile" -> basePath = parser.getAttributeValue(null, "base_path") ?: ""
                        "item" -> {
                            val id = parser.getAttributeValue(null, "id") ?: ""
                            val name = parser.getAttributeValue(null, "name") ?: ""
                            val description = parser.getAttributeValue(null, "description") ?: ""
                            val image = parser.getAttributeValue(null, "image") ?: ""
                            val newAiInfo = AiInfo(id, "$basePath/$image", name, description)
                            mapping[id] =  newAiInfo } } }
                eventType = parser.next() }
        }
    }

    fun getImagePath(name: String): AiInfo? {
        return mapping[name]
    }

    fun getAiMap(): MutableMap<String, AiInfo> {
        return mapping
    }

    fun getAiList(): List<AiInfo> {
        return mapping.values.toList()
    }

}