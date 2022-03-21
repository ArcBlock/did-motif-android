package io.arcblock.did_motif

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.arcblock.wallet.appcommonres.view.didmotif.DIDMotif
import io.arcblock.did_motif.blockies.BlockiesData
import io.arcblock.did_motif.blockies.BlockiesIdenticon
import io.arcblock.did_motif.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
  @SuppressLint("ResourceType")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
    setContentView(binding.root)
    val address = "zjdtB1FdW7kWEkqEoQ7ZEicEvmiEu32V4D5C"
    binding.motifRg.setOnCheckedChangeListener { radioGroup, pos ->
      when(pos){
        1 ->     binding.motifCircle.setAddress(address, DIDMotif.Shape.Circle)
        2 ->     binding.motifCircle.setAddress(address, DIDMotif.Shape.Square)
        3 ->     binding.motifCircle.setAddress(address, DIDMotif.Shape.Hexagon)
        else ->  binding.motifCircle.setAddress(address, DIDMotif.Shape.Rectangle)
      }
    }
    binding.motifRg.check(1)

    val ethAddress = "0xF194aec2428EE59eAC33e2e1fB37A7cD7c5440A2"
    binding.identiconRg.setOnCheckedChangeListener { radioGroup, i ->
      when(i) {
        5 -> binding.identicon.setAddress(ethAddress,BlockiesIdenticon.SHAPE_CIRCLE, BlockiesData.DEFAULT_SIZE, 0f)
        else -> binding.identicon.setAddress(ethAddress,BlockiesIdenticon.SHAPE_SQUARE, BlockiesData.DEFAULT_SIZE, 20f)
      }
    }
    binding.identiconRg.check(5)

  }
}