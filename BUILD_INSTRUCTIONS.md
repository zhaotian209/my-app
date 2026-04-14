# 房产视频一键包装项目 - 构建说明

## 构建方法

### 方法一：使用Android Studio（推荐）

1. **打开Android Studio**
2. **导入项目**：`File -> Open` 选择项目根目录 `C:\Users\天宇\Desktop\新建文件夹 (11)`
3. **等待Gradle同步**：Android Studio会自动下载并配置Gradle
4. **构建APK**：`Build -> Build Bundle(s) / APK(s) -> Build APK(s)`
5. **查看APK**：构建完成后，点击 "locate" 按钮查看生成的APK文件

### 方法二：使用命令行（需要Gradle安装）

1. **安装Gradle**：下载并安装 Gradle 8.5 版本
   - 下载地址：https://services.gradle.org/distributions/gradle-8.5-bin.zip
   - 解压到本地目录，例如 `C:\Gradle\gradle-8.5`
   - 将 `C:\Gradle\gradle-8.5\bin` 添加到系统环境变量 PATH 中

2. **执行构建**：
   ```powershell
   # 进入项目根目录
   cd C:\Users\天宇\Desktop\新建文件夹 (11)
   
   # 构建Debug版本
   gradle assembleDebug
   
   # 构建Release版本
   gradle assembleRelease
   ```

## 项目结构

- **app/src/main/java**：Java/Kotlin源代码
- **app/src/main/res**：资源文件
- **app/src/main/AndroidManifest.xml**：应用清单
- **app/build.gradle.kts**：应用级Gradle配置
- **build.gradle.kts**：项目级Gradle配置
- **settings.gradle.kts**：项目设置

## 核心功能

- ✅ 智能文案生成
- ✅ AI配音
- ✅ 视频封面提取
- ✅ 视频包装与导出
- ✅ 批量处理
- ✅ 黑金高端UI设计

## 技术栈

- Kotlin 1.9.20
- Compose Material3 1.2.0
- Clean架构
- Hilt 2.48
- Room 2.6.1
- FFmpegKit 6.0-2
- Coil 2.5.0
- WorkManager 2.9.0

## 注意事项

- 项目使用JDK 17
- 最低SDK版本：26
- 目标SDK版本：34
- 编译SDK版本：34
