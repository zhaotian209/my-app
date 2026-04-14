# GitHub Actions 云构建使用说明

## 📋 前提条件

1. 你需要有一个 GitHub 账号
2. 需要安装 Git (如果还没有的话)

## 🚀 使用步骤

### 第一步:创建 GitHub 仓库

1. 访问 https://github.com/new
2. 创建一个新的仓库(可以是公开的或私有的)
3. **不要**勾选"Initialize this repository with a README"

### 第二步:推送代码到 GitHub

在 PowerShell 中执行以下命令(替换 `你的用户名` 和 `仓库名`):

```powershell
# 添加所有文件
git add .

# 提交更改
git commit -m "Initial commit: Android project with GitHub Actions"

# 添加远程仓库(替换为你的仓库地址)
git remote add origin https://github.com/你的用户名/仓库名.git

# 推送到 GitHub
git push -u origin main
```

如果遇到分支名称问题,可以使用:
```powershell
git branch -M main
git push -u origin main
```

### 第三步:查看构建状态

1. 推送代码后,访问你的 GitHub 仓库
2. 点击顶部的 **"Actions"** 标签
3. 你会看到构建任务正在运行
4. 等待构建完成(通常需要 5-10 分钟)

### 第四步:下载 APK

构建成功后:

1. 在 Actions 页面,点击最新的构建任务
2. 在页面底部的 **"Artifacts"** 部分
3. 点击 **"app-debug-apk"** 下载调试版 APK
4. 点击 **"app-release-apk"** 下载发布版 APK

## 🔄 自动触发

每次你执行以下操作时,都会自动触发构建:

- ✅ 推送代码到 main 或 master 分支
- ✅ 创建 Pull Request
- ✅ 手动触发(在 Actions 页面点击 "Run workflow")

## 💡 提示

1. **首次构建可能较慢**: 因为需要下载依赖,后续构建会更快
2. **APK 保留时间**: 构建产物会保留 30 天
3. **构建历史**: 可以在 Actions 页面查看所有构建历史
4. **失败排查**: 如果构建失败,可以点击任务查看详细日志

## ⚙️ 自定义配置

如果需要修改构建配置,编辑文件:
`.github/workflows/android-build.yml`

常见修改:
- 更改 JDK 版本
- 添加签名配置
- 修改触发条件
- 添加测试步骤

## 🆘 常见问题

**Q: 构建失败了怎么办?**
A: 点击失败的构建任务,查看日志中的错误信息

**Q: 如何只构建 Debug 版本?**
A: 编辑 `.github/workflows/android-build.yml`,删除 Release 构建相关的步骤

**Q: APK 在哪里?**
A: 在 Actions → 构建任务 → Artifacts 中下载

**Q: 可以定时自动构建吗?**
A: 可以,在 yml 文件中添加 cron 表达式

## 📞 需要帮助?

如果遇到问题,可以:
1. 查看 GitHub Actions 文档: https://docs.github.com/en/actions
2. 查看构建日志排查问题
3. 联系技术支持
