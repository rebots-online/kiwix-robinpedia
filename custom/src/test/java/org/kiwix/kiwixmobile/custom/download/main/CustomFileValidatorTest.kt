/*
 * Kiwix Android
 * Copyright (c) 2024 Kiwix <android.kiwix.org>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.kiwix.kiwixmobile.custom.download.main

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kiwix.kiwixmobile.custom.main.CustomFileValidator
import org.kiwix.kiwixmobile.custom.main.ValidationState
import java.io.File

class CustomFileValidatorTest {

  private lateinit var context: Context
  private lateinit var customFileValidator: CustomFileValidator
  private lateinit var assetManager: AssetManager

  @BeforeEach
  fun setUp() {
    context = mockk(relaxed = true)
    assetManager = mockk(relaxed = true)
    customFileValidator = CustomFileValidator(context)
  }

  @Test
  fun `validate should call onFilesFound when both OBB and ZIM files are found`() {
    val obbFile = mockk<File>()
    val zimFile = mockk<File>()
    mockZimFiles(arrayOf(obbFile), obbFile, "obb")
    mockZimFiles(arrayOf(zimFile), zimFile, "zim")

    customFileValidator.validate(
      onFilesFound = {
        assertTrue(it is ValidationState.HasBothFiles)
        assertEquals(obbFile, (it as ValidationState.HasBothFiles).obbFile)
        assertEquals(zimFile, it.zimFile)
      },
      onNoFilesFound = { fail("Should not call onNoFilesFound") }
    )
  }

  @Test
  fun `validate should call onFilesFound when only OBB file is found`() {
    val obbFile = mockk<File>()
    mockZimFiles(arrayOf(obbFile), obbFile, "obb")
    mockZimFiles(emptyArray(), obbFile, "zim")

    customFileValidator.validate(
      onFilesFound = {
        assertTrue(it is ValidationState.HasFile)
        assertEquals(obbFile, (it as ValidationState.HasFile).file)
      },
      onNoFilesFound = { fail("Should not call onNoFilesFound") }
    )
  }

  @Test
  fun `validate should call onFilesFound when only ZIM file is found`() {
    val zimFile = mockk<File>()
    mockZimFiles(emptyArray(), zimFile, "obb")
    mockZimFiles(arrayOf(zimFile), zimFile, "zim")

    customFileValidator.validate(
      onFilesFound = {
        assertTrue(it is ValidationState.HasFile)
        assertEquals(zimFile, (it as ValidationState.HasFile).file)
      },
      onNoFilesFound = { fail("Should not call onNoFilesFound") }
    )
  }

  @Test
  fun `validate should call onNoFilesFound when no OBB or ZIM files are found`() {
    mockZimFiles(emptyArray(), mockk(), extension = "zim")
    mockZimFiles(emptyArray(), mockk(), extension = "obb")

    customFileValidator.validate(
      onFilesFound = { fail("Should not call onFilesFound") },
      onNoFilesFound = { /* Success */ }
    )
  }

  @Test
  fun `getAssetFileDescriptorListFromPlayAssetDelivery returns empty list when exception occurs`() {
    every {
      context.createPackageContext(
        any(),
        any()
      ).assets
    } throws PackageManager.NameNotFoundException()

    val assetList = customFileValidator.getAssetFileDescriptorListFromPlayAssetDelivery()

    assertTrue(assetList.isEmpty())
  }

  @Test
  fun `getAssetFileDescriptorListFromPlayAssetDelivery returns list of asset descriptors`() {
    val descriptor = mockk<AssetFileDescriptor>()
    every { context.createPackageContext(any(), any()).assets } returns assetManager
    every { assetManager.openFd(any()) } returns descriptor
    every { assetManager.list("") } returns arrayOf("chunk1.zim", "chunk2.zim")

    val assetList = customFileValidator.getAssetFileDescriptorListFromPlayAssetDelivery()

    assertEquals(2, assetList.size)
    assertEquals(descriptor, assetList[0])
  }

  private fun mockZimFiles(
    storageDirectory: Array<File?>,
    zimFile: File,
    extension: String
  ) {
    every { zimFile.exists() } returns true
    every { zimFile.isFile } returns true
    every { zimFile.extension } returns extension
    mockkStatic(File::walk)
    storageDirectory.forEach { dir ->
      dir?.let {
        every { dir.exists() } returns true
        every { dir.isDirectory } returns true
        every { dir.extension } returns ""
        every { dir.parent } returns null
        every { dir.listFiles() } returns arrayOf(zimFile)
        every { dir.walk() } answers { mockFileWalk(listOf(zimFile)) }
      }
    }

    if (extension == "zim") {
      every { ContextCompat.getExternalFilesDirs(context, null) } returns storageDirectory
    } else {
      every { ContextCompat.getObbDirs(context) } returns storageDirectory
    }
  }

  private fun mockFileWalk(files: List<File>): FileTreeWalk {
    val fileTreeWalk = mockk<FileTreeWalk>()
    every { fileTreeWalk.iterator() } returns files.iterator()
    return fileTreeWalk
  }
}
