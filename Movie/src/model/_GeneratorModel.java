package model;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

public class _GeneratorModel
{

	public static DataSource getDataSource()
	{
		Prop p = PropKit.use("prop.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.get("url"),
				p.get("user"), p.get("password").trim());
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public static void main(String[] args)
	{
		// base model 所使用的包名
		String baseModelPackageName = "model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getRootClassPath()
				+ "/../../../src/model/base";
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";
		System.out.println(baseModelOutputDir);

		// 创建生成器
		Generator gernerator = new Generator(getDataSource(),
				baseModelPackageName, baseModelOutputDir, modelPackageName,
				modelOutputDir);

		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
		// gernerator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName
		// gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}
}
