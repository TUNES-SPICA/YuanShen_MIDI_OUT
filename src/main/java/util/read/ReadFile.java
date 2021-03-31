package util.read;

import entity.YuanShenEntity;

/**
 *
 */
public interface ReadFile {

    /**
     * 读取文件，返回用于输出的文件实体
     *
     * @param fileURL 文件路径
     * @return 音乐实体
     */
    YuanShenEntity readMML(String fileURL);

}


