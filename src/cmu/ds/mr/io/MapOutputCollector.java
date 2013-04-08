package cmu.ds.mr.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MapOutputCollector implements OutputCollector<String, Long> {

  private List<Map<String, Long>> outlist;

  private int numRed;

  private String basePath;

  public MapOutputCollector(String basePath, int nred) throws IOException {
    this.basePath = basePath;
    numRed = nred;
    outlist = new ArrayList<Map<String, Long>>();
    for (int i = 0; i < numRed; i++) {
      outlist.add(new TreeMap<String, Long>());
    }
  }

  @Override
  public void collect(String key, Long value) throws IOException {
    // Assume key is String
    int k = key.toString().hashCode() % numRed;
    outlist.get(k).put(key, value);
  }

  public void writeToDisk() throws IOException {
    for (int i = 0; i < numRed; i++) {
      BufferedWriter bw = new BufferedWriter(
              new FileWriter(basePath + File.pathSeparatorChar + i));
      try {
        Map<String, Long> map = outlist.get(i);
        for (Entry<String, Long> en : map.entrySet()) {
          bw.write(String.format("%s\t%d\n", en.getKey(), en.getValue()));
        }
      } finally {
        bw.close();
      }
    }
  }

}
