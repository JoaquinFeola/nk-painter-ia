#include <jni.h>
#include <stdlib.h>

/* calloc, not malloc: a new tile has to start transparent, as it does on the JVM. */
JNIEXPORT jobject JNICALL
Java_com_nkpainter_nativememory_NativeMemory_allocate(JNIEnv *env, jobject self, jint byte_count) {
    void *memory = calloc((size_t) byte_count, 1);
    if (memory == NULL) {
        return NULL;
    }
    return (*env)->NewDirectByteBuffer(env, memory, (jlong) byte_count);
}

JNIEXPORT void JNICALL
Java_com_nkpainter_nativememory_NativeMemory_free(JNIEnv *env, jobject self, jobject buffer) {
    free((*env)->GetDirectBufferAddress(env, buffer));
}
