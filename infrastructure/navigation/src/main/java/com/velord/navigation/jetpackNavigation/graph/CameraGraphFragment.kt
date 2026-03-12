<<<<<<<< HEAD:app/src/main/java/com/velord/composescreenexample/ui/main/bottomNav/centerGraph/CenterGraphFragment.kt
package com.velord.composescreenexample.ui.main.bottomNav.centerGraph
========
package com.velord.navigation.jetpackNavigation.graph
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:infrastructure/navigation/src/main/java/com/velord/navigation/jetpackNavigation/graph/CameraGraphFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.velord.core.ui.utils.setContentWithTheme
import com.velord.navigation.R

<<<<<<<< HEAD:app/src/main/java/com/velord/composescreenexample/ui/main/bottomNav/centerGraph/CenterGraphFragment.kt
@AndroidEntryPoint
class CenterGraphFragment : Fragment() {
========
class CameraGraphFragment : Fragment() {
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:infrastructure/navigation/src/main/java/com/velord/navigation/jetpackNavigation/graph/CameraGraphFragment.kt

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.findNavController().navigate(R.id.from_cameraGraphFragment_to_CameraRecordingFragment)
    }
}