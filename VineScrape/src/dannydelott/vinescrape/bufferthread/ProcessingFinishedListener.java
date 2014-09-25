package dannydelott.vinescrape.bufferthread;

import java.util.HashSet;

public interface ProcessingFinishedListener {
	public void onProcessFinished(int vinesScraped, int vinesInFile,
			String output, BufferType buffer, HashSet<String> duplicateUrls,
			HashSet<String> posTags, HashSet<String> relationTags);
}
