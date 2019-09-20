# uranus-spring-boot-starter

### AOP: 
  1. 日志切面：显示请求和返回的详细信息
  2. controller切面：对所有controller文件夹下的请求进行参数校验，VO对象参数校验必须绑定BindingResult

### Redis
  1. RedisBaseDao: pojo/list/map的增删改查，默认有效期2天
  2. RedisConfig: 整合Jackson进行序列化和反序列化

### Swagger
  1. 在线文档： 自动显示
  2. 离线文档： 通过Mock读取请求生成文档- Mock -> JSON -> ADOC -> HTML5/PDF

### Web
  1. controller: 控制器全局异常控制
  2. filter: 跨域访问
  3. rest: json数据返回整合Jackson

### 工具类
  1. 文件拷贝
  2. 距离计算
  3. 时间函数
  4. 加解密
  5. IP读取
  6. 主键生成
  7. 字符串判断
  8. 数字处理
  9. 代理判断
  
### 使用说明
  1. aop
      1. 开关：uranus.aop.enable:false/true 默认开起
      2. log: @ControllerLog @ServiceLog 分别放在controller层和service层
      3. vaild: @DateRangCheck 时间校验注解，验证两个时间段是否合规
  2. Redis
      1. 开关: uranus.redis.enable:false/true 默认开起
      2. 使用: 继承 RedisBaseDao<T> 
  3. Swagger
      1. 开关: uranus.swagger.enable:false/true 默认关闭
      2. 参数: 
          1. uranus.swagger.title 标题
          2. uranus.swagger.description 描述
          3. uranus.swagger.termsOfServiceUrl 条款地址，公司内部使用的话不需要配
          4. uranus.swagger.license 协议名称
          5. uranus.swagger.licenseUrl 协议地址
          6. uranus.swagger.version 版本
          7. uranus.swagger.host 项目地址 + 端口 示例 localhost:8080
          8. uranus.swagger.filePath Swagger生成文件存放路径
          9. uranus.swagger.fileType 生成文件的类型 html5 / pdf
      3. 使用 运行test即可生成文档
      ```
        @RunWith(SpringRunner.class)
        @SpringBootTest
        public class Swagger2DocTest {

            @Autowired
            private Swagger2Doc doc;

            @Test
            public void createSwaggerDoc() throws Exception {
                doc.createDoc();
            }
        }
      ```
    4. web
        1. 开关：uranus.web.enable:false/true 默认关闭
