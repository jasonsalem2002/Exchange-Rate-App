
```
ExchangeRate-7
├─ Backend
│  ├─ __init__.py
│  ├─ app.py
│  ├─ config.py
│  ├─ models
│  │  ├─ Transaction.py
│  │  ├─ User.py
│  │  └─ __init__.py
│  ├─ routes
│  │  ├─ __init__.py
│  │  ├─ authenticationRoute.py
│  │  ├─ exchangeRateRoute.py
│  │  ├─ transactionRoute.py
│  │  └─ userRoute.py
│  ├─ schemas
│  │  ├─ __init__.py
│  │  ├─ transactionSchema.py
│  │  └─ userSchema.py
│  └─ utils
│     ├─ __init__.py
│     └─ util.py
└─ README.md

```
```
ExchangeRate-7
├
│  │        │  ├─ depends.py
│  │        │  ├─ dist.py
│  │        │  ├─ errors.py
│  │        │  ├─ extension.py
│  │        │  ├─ extern
│  │        │  │  └─ __init__.py
│  │        │  ├─ glob.py
│  │        │  ├─ gui-32.exe
│  │        │  ├─ gui-64.exe
│  │        │  ├─ gui.exe
│  │        │  ├─ installer.py
│  │        │  ├─ launch.py
│  │        │  ├─ monkey.py
│  │        │  ├─ msvc.py
│  │        │  ├─ namespaces.py
│  │        │  ├─ package_index.py
│  │        │  ├─ py34compat.py
│  │        │  ├─ sandbox.py
│  │        │  ├─ script (dev).tmpl
│  │        │  ├─ script.tmpl
│  │        │  ├─ unicode_utils.py
│  │        │  ├─ version.py
│  │        │  ├─ wheel.py
│  │        │  └─ windows_support.py
│  │        ├─ setuptools-58.0.4.dist-info
│  │        │  ├─ INSTALLER
│  │        │  ├─ LICENSE
│  │        │  ├─ METADATA
│  │        │  ├─ RECORD
│  │        │  ├─ REQUESTED
│  │        │  ├─ WHEEL
│  │        │  ├─ entry_points.txt
│  │        │  └─ top_level.txt
│  │        ├─ six-1.16.0.dist-info
│  │        │  ├─ INSTALLER
│  │        │  ├─ LICENSE
│  │        │  ├─ METADATA
│  │        │  ├─ RECORD
│  │        │  ├─ WHEEL
│  │        │  └─ top_level.txt
│  │        ├─ six.py
│  │        ├─ sqlalchemy
│  │        │  ├─ __init__.py
│  │        │  ├─ connectors
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ aioodbc.py
│  │        │  │  ├─ asyncio.py
│  │        │  │  └─ pyodbc.py
│  │        │  ├─ cyextension
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ collections.cpython-39-darwin.so
│  │        │  │  ├─ collections.pyx
│  │        │  │  ├─ immutabledict.cpython-39-darwin.so
│  │        │  │  ├─ immutabledict.pxd
│  │        │  │  ├─ immutabledict.pyx
│  │        │  │  ├─ processors.cpython-39-darwin.so
│  │        │  │  ├─ processors.pyx
│  │        │  │  ├─ resultproxy.cpython-39-darwin.so
│  │        │  │  ├─ resultproxy.pyx
│  │        │  │  ├─ util.cpython-39-darwin.so
│  │        │  │  └─ util.pyx
│  │        │  ├─ dialects
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ _typing.py
│  │        │  │  ├─ mssql
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ aioodbc.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ information_schema.py
│  │        │  │  │  ├─ json.py
│  │        │  │  │  ├─ provision.py
│  │        │  │  │  ├─ pymssql.py
│  │        │  │  │  └─ pyodbc.py
│  │        │  │  ├─ mysql
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ aiomysql.py
│  │        │  │  │  ├─ asyncmy.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ cymysql.py
│  │        │  │  │  ├─ dml.py
│  │        │  │  │  ├─ enumerated.py
│  │        │  │  │  ├─ expression.py
│  │        │  │  │  ├─ json.py
│  │        │  │  │  ├─ mariadb.py
│  │        │  │  │  ├─ mariadbconnector.py
│  │        │  │  │  ├─ mysqlconnector.py
│  │        │  │  │  ├─ mysqldb.py
│  │        │  │  │  ├─ provision.py
│  │        │  │  │  ├─ pymysql.py
│  │        │  │  │  ├─ pyodbc.py
│  │        │  │  │  ├─ reflection.py
│  │        │  │  │  ├─ reserved_words.py
│  │        │  │  │  └─ types.py
│  │        │  │  ├─ oracle
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ cx_oracle.py
│  │        │  │  │  ├─ dictionary.py
│  │        │  │  │  ├─ oracledb.py
│  │        │  │  │  ├─ provision.py
│  │        │  │  │  └─ types.py
│  │        │  │  ├─ postgresql
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ _psycopg_common.py
│  │        │  │  │  ├─ array.py
│  │        │  │  │  ├─ asyncpg.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ dml.py
│  │        │  │  │  ├─ ext.py
│  │        │  │  │  ├─ hstore.py
│  │        │  │  │  ├─ json.py
│  │        │  │  │  ├─ named_types.py
│  │        │  │  │  ├─ operators.py
│  │        │  │  │  ├─ pg8000.py
│  │        │  │  │  ├─ pg_catalog.py
│  │        │  │  │  ├─ provision.py
│  │        │  │  │  ├─ psycopg.py
│  │        │  │  │  ├─ psycopg2.py
│  │        │  │  │  ├─ psycopg2cffi.py
│  │        │  │  │  ├─ ranges.py
│  │        │  │  │  └─ types.py
│  │        │  │  ├─ sqlite
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ aiosqlite.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ dml.py
│  │        │  │  │  ├─ json.py
│  │        │  │  │  ├─ provision.py
│  │        │  │  │  ├─ pysqlcipher.py
│  │        │  │  │  └─ pysqlite.py
│  │        │  │  └─ type_migration_guidelines.txt
│  │        │  ├─ engine
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ _py_processors.py
│  │        │  │  ├─ _py_row.py
│  │        │  │  ├─ _py_util.py
│  │        │  │  ├─ base.py
│  │        │  │  ├─ characteristics.py
│  │        │  │  ├─ create.py
│  │        │  │  ├─ cursor.py
│  │        │  │  ├─ default.py
│  │        │  │  ├─ events.py
│  │        │  │  ├─ interfaces.py
│  │        │  │  ├─ mock.py
│  │        │  │  ├─ processors.py
│  │        │  │  ├─ reflection.py
│  │        │  │  ├─ result.py
│  │        │  │  ├─ row.py
│  │        │  │  ├─ strategies.py
│  │        │  │  ├─ url.py
│  │        │  │  └─ util.py
│  │        │  ├─ event
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ api.py
│  │        │  │  ├─ attr.py
│  │        │  │  ├─ base.py
│  │        │  │  ├─ legacy.py
│  │        │  │  └─ registry.py
│  │        │  ├─ events.py
│  │        │  ├─ exc.py
│  │        │  ├─ ext
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ associationproxy.py
│  │        │  │  ├─ asyncio
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ engine.py
│  │        │  │  │  ├─ exc.py
│  │        │  │  │  ├─ result.py
│  │        │  │  │  ├─ scoping.py
│  │        │  │  │  └─ session.py
│  │        │  │  ├─ automap.py
│  │        │  │  ├─ baked.py
│  │        │  │  ├─ compiler.py
│  │        │  │  ├─ declarative
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  └─ extensions.py
│  │        │  │  ├─ horizontal_shard.py
│  │        │  │  ├─ hybrid.py
│  │        │  │  ├─ indexable.py
│  │        │  │  ├─ instrumentation.py
│  │        │  │  ├─ mutable.py
│  │        │  │  ├─ mypy
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ apply.py
│  │        │  │  │  ├─ decl_class.py
│  │        │  │  │  ├─ infer.py
│  │        │  │  │  ├─ names.py
│  │        │  │  │  ├─ plugin.py
│  │        │  │  │  └─ util.py
│  │        │  │  ├─ orderinglist.py
│  │        │  │  └─ serializer.py
│  │        │  ├─ future
│  │        │  │  ├─ __init__.py
│  │        │  │  └─ engine.py
│  │        │  ├─ inspection.py
│  │        │  ├─ log.py
│  │        │  ├─ orm
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ _orm_constructors.py
│  │        │  │  ├─ _typing.py
│  │        │  │  ├─ attributes.py
│  │        │  │  ├─ base.py
│  │        │  │  ├─ bulk_persistence.py
│  │        │  │  ├─ clsregistry.py
│  │        │  │  ├─ collections.py
│  │        │  │  ├─ context.py
│  │        │  │  ├─ decl_api.py
│  │        │  │  ├─ decl_base.py
│  │        │  │  ├─ dependency.py
│  │        │  │  ├─ descriptor_props.py
│  │        │  │  ├─ dynamic.py
│  │        │  │  ├─ evaluator.py
│  │        │  │  ├─ events.py
│  │        │  │  ├─ exc.py
│  │        │  │  ├─ identity.py
│  │        │  │  ├─ instrumentation.py
│  │        │  │  ├─ interfaces.py
│  │        │  │  ├─ loading.py
│  │        │  │  ├─ mapped_collection.py
│  │        │  │  ├─ mapper.py
│  │        │  │  ├─ path_registry.py
│  │        │  │  ├─ persistence.py
│  │        │  │  ├─ properties.py
│  │        │  │  ├─ query.py
│  │        │  │  ├─ relationships.py
│  │        │  │  ├─ scoping.py
│  │        │  │  ├─ session.py
│  │        │  │  ├─ state.py
│  │        │  │  ├─ state_changes.py
│  │        │  │  ├─ strategies.py
│  │        │  │  ├─ strategy_options.py
│  │        │  │  ├─ sync.py
│  │        │  │  ├─ unitofwork.py
│  │        │  │  ├─ util.py
│  │        │  │  └─ writeonly.py
│  │        │  ├─ pool
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ base.py
│  │        │  │  ├─ events.py
│  │        │  │  └─ impl.py
│  │        │  ├─ py.typed
│  │        │  ├─ schema.py
│  │        │  ├─ sql
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ _dml_constructors.py
│  │        │  │  ├─ _elements_constructors.py
│  │        │  │  ├─ _orm_types.py
│  │        │  │  ├─ _py_util.py
│  │        │  │  ├─ _selectable_constructors.py
│  │        │  │  ├─ _typing.py
│  │        │  │  ├─ annotation.py
│  │        │  │  ├─ base.py
│  │        │  │  ├─ cache_key.py
│  │        │  │  ├─ coercions.py
│  │        │  │  ├─ compiler.py
│  │        │  │  ├─ crud.py
│  │        │  │  ├─ ddl.py
│  │        │  │  ├─ default_comparator.py
│  │        │  │  ├─ dml.py
│  │        │  │  ├─ elements.py
│  │        │  │  ├─ events.py
│  │        │  │  ├─ expression.py
│  │        │  │  ├─ functions.py
│  │        │  │  ├─ lambdas.py
│  │        │  │  ├─ naming.py
│  │        │  │  ├─ operators.py
│  │        │  │  ├─ roles.py
│  │        │  │  ├─ schema.py
│  │        │  │  ├─ selectable.py
│  │        │  │  ├─ sqltypes.py
│  │        │  │  ├─ traversals.py
│  │        │  │  ├─ type_api.py
│  │        │  │  ├─ util.py
│  │        │  │  └─ visitors.py
│  │        │  ├─ testing
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ assertions.py
│  │        │  │  ├─ assertsql.py
│  │        │  │  ├─ asyncio.py
│  │        │  │  ├─ config.py
│  │        │  │  ├─ engines.py
│  │        │  │  ├─ entities.py
│  │        │  │  ├─ exclusions.py
│  │        │  │  ├─ fixtures
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ base.py
│  │        │  │  │  ├─ mypy.py
│  │        │  │  │  ├─ orm.py
│  │        │  │  │  └─ sql.py
│  │        │  │  ├─ pickleable.py
│  │        │  │  ├─ plugin
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ bootstrap.py
│  │        │  │  │  ├─ plugin_base.py
│  │        │  │  │  └─ pytestplugin.py
│  │        │  │  ├─ profiling.py
│  │        │  │  ├─ provision.py
│  │        │  │  ├─ requirements.py
│  │        │  │  ├─ schema.py
│  │        │  │  ├─ suite
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ test_cte.py
│  │        │  │  │  ├─ test_ddl.py
│  │        │  │  │  ├─ test_deprecations.py
│  │        │  │  │  ├─ test_dialect.py
│  │        │  │  │  ├─ test_insert.py
│  │        │  │  │  ├─ test_reflection.py
│  │        │  │  │  ├─ test_results.py
│  │        │  │  │  ├─ test_rowcount.py
│  │        │  │  │  ├─ test_select.py
│  │        │  │  │  ├─ test_sequence.py
│  │        │  │  │  ├─ test_types.py
│  │        │  │  │  ├─ test_unicode_ddl.py
│  │        │  │  │  └─ test_update_delete.py
│  │        │  │  ├─ util.py
│  │        │  │  └─ warnings.py
│  │        │  ├─ types.py
│  │        │  └─ util
│  │        │     ├─ __init__.py
│  │        │     ├─ _collections.py
│  │        │     ├─ _concurrency_py3k.py
│  │        │     ├─ _has_cy.py
│  │        │     ├─ _py_collections.py
│  │        │     ├─ compat.py
│  │        │     ├─ concurrency.py
│  │        │     ├─ deprecations.py
│  │        │     ├─ langhelpers.py
│  │        │     ├─ preloaded.py
│  │        │     ├─ queue.py
│  │        │     ├─ tool_support.py
│  │        │     ├─ topological.py
│  │        │     └─ typing.py
│  │        ├─ typing_extensions-4.10.0.dist-info
│  │        │  ├─ INSTALLER
│  │        │  ├─ LICENSE
│  │        │  ├─ METADATA
│  │        │  ├─ RECORD
│  │        │  ├─ REQUESTED
│  │        │  └─ WHEEL
│  │        ├─ typing_extensions.py
│  │        ├─ urllib3
│  │        │  ├─ __init__.py
│  │        │  ├─ _base_connection.py
│  │        │  ├─ _collections.py
│  │        │  ├─ _request_methods.py
│  │        │  ├─ _version.py
│  │        │  ├─ connection.py
│  │        │  ├─ connectionpool.py
│  │        │  ├─ contrib
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ emscripten
│  │        │  │  │  ├─ __init__.py
│  │        │  │  │  ├─ connection.py
│  │        │  │  │  ├─ emscripten_fetch_worker.js
│  │        │  │  │  ├─ fetch.py
│  │        │  │  │  ├─ request.py
│  │        │  │  │  └─ response.py
│  │        │  │  ├─ pyopenssl.py
│  │        │  │  └─ socks.py
│  │        │  ├─ exceptions.py
│  │        │  ├─ fields.py
│  │        │  ├─ filepost.py
│  │        │  ├─ http2.py
│  │        │  ├─ poolmanager.py
│  │        │  ├─ py.typed
│  │        │  ├─ response.py
│  │        │  └─ util
│  │        │     ├─ __init__.py
│  │        │     ├─ connection.py
│  │        │     ├─ proxy.py
│  │        │     ├─ request.py
│  │        │     ├─ response.py
│  │        │     ├─ retry.py
│  │        │     ├─ ssl_.py
│  │        │     ├─ ssl_match_hostname.py
│  │        │     ├─ ssltransport.py
│  │        │     ├─ timeout.py
│  │        │     ├─ url.py
│  │        │     ├─ util.py
│  │        │     └─ wait.py
│  │        ├─ urllib3-2.2.1.dist-info
│  │        │  ├─ INSTALLER
│  │        │  ├─ METADATA
│  │        │  ├─ RECORD
│  │        │  ├─ WHEEL
│  │        │  └─ licenses
│  │        │     └─ LICENSE.txt
│  │        ├─ werkzeug
│  │        │  ├─ __init__.py
│  │        │  ├─ _internal.py
│  │        │  ├─ _reloader.py
│  │        │  ├─ datastructures
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ accept.py
│  │        │  │  ├─ accept.pyi
│  │        │  │  ├─ auth.py
│  │        │  │  ├─ cache_control.py
│  │        │  │  ├─ cache_control.pyi
│  │        │  │  ├─ csp.py
│  │        │  │  ├─ csp.pyi
│  │        │  │  ├─ etag.py
│  │        │  │  ├─ etag.pyi
│  │        │  │  ├─ file_storage.py
│  │        │  │  ├─ file_storage.pyi
│  │        │  │  ├─ headers.py
│  │        │  │  ├─ headers.pyi
│  │        │  │  ├─ mixins.py
│  │        │  │  ├─ mixins.pyi
│  │        │  │  ├─ range.py
│  │        │  │  ├─ range.pyi
│  │        │  │  ├─ structures.py
│  │        │  │  └─ structures.pyi
│  │        │  ├─ debug
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ console.py
│  │        │  │  ├─ repr.py
│  │        │  │  ├─ shared
│  │        │  │  │  ├─ ICON_LICENSE.md
│  │        │  │  │  ├─ console.png
│  │        │  │  │  ├─ debugger.js
│  │        │  │  │  ├─ less.png
│  │        │  │  │  ├─ more.png
│  │        │  │  │  └─ style.css
│  │        │  │  └─ tbtools.py
│  │        │  ├─ exceptions.py
│  │        │  ├─ formparser.py
│  │        │  ├─ http.py
│  │        │  ├─ local.py
│  │        │  ├─ middleware
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ dispatcher.py
│  │        │  │  ├─ http_proxy.py
│  │        │  │  ├─ lint.py
│  │        │  │  ├─ profiler.py
│  │        │  │  ├─ proxy_fix.py
│  │        │  │  └─ shared_data.py
│  │        │  ├─ py.typed
│  │        │  ├─ routing
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ converters.py
│  │        │  │  ├─ exceptions.py
│  │        │  │  ├─ map.py
│  │        │  │  ├─ matcher.py
│  │        │  │  └─ rules.py
│  │        │  ├─ sansio
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ http.py
│  │        │  │  ├─ multipart.py
│  │        │  │  ├─ request.py
│  │        │  │  ├─ response.py
│  │        │  │  └─ utils.py
│  │        │  ├─ security.py
│  │        │  ├─ serving.py
│  │        │  ├─ test.py
│  │        │  ├─ testapp.py
│  │        │  ├─ urls.py
│  │        │  ├─ user_agent.py
│  │        │  ├─ utils.py
│  │        │  ├─ wrappers
│  │        │  │  ├─ __init__.py
│  │        │  │  ├─ request.py
│  │        │  │  └─ response.py
│  │        │  └─ wsgi.py
│  │        ├─ werkzeug-3.0.1.dist-info
│  │        │  ├─ INSTALLER
│  │        │  ├─ LICENSE.rst
│  │        │  ├─ METADATA
│  │        │  ├─ RECORD
│  │        │  ├─ REQUESTED
│  │        │  └─ WHEEL
│  │        ├─ zipp
│  │        │  ├─ __init__.py
│  │        │  ├─ compat
│  │        │  │  ├─ __init__.py
│  │        │  │  └─ py310.py
│  │        │  └─ glob.py
│  │        └─ zipp-3.18.1.dist-info
│  │           ├─ INSTALLER
│  │           ├─ LICENSE
│  │           ├─ METADATA
│  │           ├─ RECORD
│  │           ├─ REQUESTED
│  │           ├─ WHEEL
│  │           └─ top_level.txt
│  └─ pyvenv.cfg
├─ Android
│  └─ exchange-android-EricNJ
│     ├─ .gitignore
│     ├─ app
│     │  ├─ .gitignore
│     │  ├─ build.gradle
│     │  ├─ proguard-rules.pro
│     │  └─ src
│     │     ├─ androidTest
│     │     │  └─ java
│     │     │     └─ com
│     │     │        └─ example
│     │     │           └─ currencyexchange
│     │     │              └─ ExampleInstrumentedTest.kt
│     │     ├─ main
│     │     │  ├─ AndroidManifest.xml
│     │     │  ├─ java
│     │     │  │  └─ com
│     │     │  │     └─ example
│     │     │  │        └─ currencyexchange
│     │     │  │           ├─ ChartFragment.kt
│     │     │  │           ├─ ExchangeFragment.kt
│     │     │  │           ├─ LoginActivity.kt
│     │     │  │           ├─ MainActivity.kt
│     │     │  │           ├─ RegistrationActivity.kt
│     │     │  │           ├─ TabsPagerAdapter.kt
│     │     │  │           ├─ TradingFragment.kt
│     │     │  │           ├─ TransactionsFragment.kt
│     │     │  │           └─ api
│     │     │  │              ├─ Authentication.kt
│     │     │  │              ├─ model
│     │     │  │              │  ├─ ExchangeRates.kt
│     │     │  │              │  ├─ Token.kt
│     │     │  │              │  ├─ Transaction.kt
│     │     │  │              │  └─ User.kt
│     │     │  │              └─ retrofit.kt
│     │     │  └─ res
│     │     │     ├─ drawable
│     │     │     │  ├─ baseline_add_24.xml
│     │     │     │  ├─ ic_chart.xml
│     │     │     │  ├─ ic_home_.xml
│     │     │     │  ├─ ic_launcher_background.xml
│     │     │     │  ├─ ic_tradingplat.xml
│     │     │     │  └─ ic_transactions.xml
│     │     │     ├─ drawable-v24
│     │     │     │  └─ ic_launcher_foreground.xml
│     │     │     ├─ layout
│     │     │     │  ├─ activity_login.xml
│     │     │     │  ├─ activity_main.xml
│     │     │     │  ├─ activity_registration.xml
│     │     │     │  ├─ dialog_trading.xml
│     │     │     │  ├─ dialog_transaction.xml
│     │     │     │  ├─ fragment_chart.xml
│     │     │     │  ├─ fragment_exchange.xml
│     │     │     │  ├─ fragment_trading.xml
│     │     │     │  ├─ fragment_transactions.xml
│     │     │     │  └─ item_transaction.xml
│     │     │     ├─ menu
│     │     │     │  ├─ bottom_nav.xml
│     │     │     │  ├─ menu_logged_in.xml
│     │     │     │  └─ menu_logged_out.xml
│     │     │     ├─ mipmap-anydpi-v26
│     │     │     │  ├─ ic_launcher.xml
│     │     │     │  └─ ic_launcher_round.xml
│     │     │     ├─ mipmap-hdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-mdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xxhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ mipmap-xxxhdpi
│     │     │     │  ├─ ic_launcher.webp
│     │     │     │  └─ ic_launcher_round.webp
│     │     │     ├─ navigation
│     │     │     │  └─ nav_graph.xml
│     │     │     ├─ values
│     │     │     │  ├─ colors.xml
│     │     │     │  ├─ strings.xml
│     │     │     │  └─ themes.xml
│     │     │     ├─ values-night
│     │     │     │  └─ themes.xml
│     │     │     └─ xml
│     │     │        ├─ backup_rules.xml
│     │     │        └─ data_extraction_rules.xml
│     │     └─ test
│     │        └─ java
│     │           └─ com
│     │              └─ example
│     │                 └─ currencyexchange
│     │                    └─ ExampleUnitTest.kt
│     ├─ build.gradle
│     ├─ gradle
│     │  ├─ build.gradle
│     │  └─ wrapper
│     │     ├─ gradle-wrapper.jar
│     │     └─ gradle-wrapper.properties
│     ├─ gradle.properties
│     ├─ gradlew
│     ├─ gradlew.bat
│     ├─ settings.gradle
│     └─ temp.txt
├─ Backend
│  ├─ __init__.py
│  ├─ app.py
│  ├─ config.py
│  ├─ models
│  │  ├─ Offer.py
│  │  ├─ Transaction.py
│  │  ├─ User.py
│  │  └─ __init__.py
│  ├─ routes
│  │  ├─ __init__.py
│  │  ├─ acceptedOfferRoute.py
│  │  ├─ authenticationRoute.py
│  │  ├─ exchangeRateRoute.py
│  │  ├─ offersRoute.py
│  │  ├─ statistics.py
│  │  ├─ transactionRoute.py
│  │  └─ userRoute.py
│  ├─ schemas
│  │  ├─ __init__.py
│  │  ├─ offerSchema.py
│  │  ├─ transactionSchema.py
│  │  └─ userSchema.py
│  └─ utils
│     ├─ __init__.py
│     └─ util.py
├─ Desktop
│  ├─ .idea
│  │  ├─ .gitignore
│  │  ├─ Desktop-App.iml
│  │  ├─ misc.xml
│  │  ├─ modules.xml
│  │  └─ vcs.xml
│  └─ exchange
│     ├─ .gitignore
│     ├─ .idea
│     │  ├─ .gitignore
│     │  ├─ .gitignore.old
│     │  ├─ encodings.xml
│     │  ├─ misc.xml
│     │  ├─ misc.xml.old
│     │  ├─ uiDesigner.xml
│     │  ├─ vcs.xml
│     │  └─ vcs.xml.old
│     ├─ .mvn
│     │  └─ wrapper
│     │     ├─ maven-wrapper.jar
│     │     └─ maven-wrapper.properties
│     ├─ exchange
│     │  ├─ .gitignore
│     │  ├─ .idea
│     │  │  ├─ .gitignore
│     │  │  ├─ encodings.xml
│     │  │  ├─ misc.xml
│     │  │  ├─ uiDesigner.xml
│     │  │  └─ vcs.xml
│     │  ├─ .mvn
│     │  │  └─ wrapper
│     │  │     ├─ maven-wrapper.jar
│     │  │     └─ maven-wrapper.properties
│     │  ├─ lib
│     │  │  ├─ converter-gson-2.9.0-javadoc.jar
│     │  │  ├─ converter-gson-2.9.0.jar
│     │  │  ├─ gson-2.8.5-javadoc.jar
│     │  │  ├─ gson-2.8.5.jar
│     │  │  ├─ okhttp-3.14.9-javadoc.jar
│     │  │  ├─ okhttp-3.14.9.jar
│     │  │  ├─ okio-1.17.2-javadoc.jar
│     │  │  ├─ okio-1.17.2.jar
│     │  │  ├─ retrofit-2.9.0-javadoc.jar
│     │  │  └─ retrofit-2.9.0.jar
│     │  ├─ mvnw
│     │  ├─ mvnw.cmd
│     │  ├─ pom.xml
│     │  └─ src
│     │     └─ main
│     │        ├─ java
│     │        │  ├─ com
│     │        │  │  └─ kjb04
│     │        │  │     └─ exchange
│     │        │  │        ├─ Main.java
│     │        │  │        ├─ Parent.java
│     │        │  │        ├─ api
│     │        │  │        │  ├─ Exchange.java
│     │        │  │        │  ├─ ExchangeService.java
│     │        │  │        │  └─ model
│     │        │  │        │     ├─ ExchangeRates.java
│     │        │  │        │     └─ Transaction.java
│     │        │  │        └─ rates
│     │        │  │           └─ Rates.java
│     │        │  └─ module-info.java
│     │        └─ resources
│     │           └─ com
│     │              └─ kjb04
│     │                 └─ exchange
│     │                    ├─ parent.css
│     │                    ├─ parent.fxml
│     │                    └─ rates
│     │                       ├─ rates.css
│     │                       └─ rates.fxml
│     ├─ lib
│     │  ├─ converter-gson-2.9.0-javadoc.jar
│     │  ├─ converter-gson-2.9.0.jar
│     │  ├─ gson-2.8.5-javadoc.jar
│     │  ├─ gson-2.8.5.jar
│     │  ├─ okhttp-3.14.9-javadoc.jar
│     │  ├─ okhttp-3.14.9.jar
│     │  ├─ okio-1.17.2-javadoc.jar
│     │  ├─ okio-1.17.2.jar
│     │  ├─ retrofit-2.9.0-javadoc.jar
│     │  └─ retrofit-2.9.0.jar
│     ├─ mvnw
│     ├─ mvnw.cmd
│     ├─ pom.xml
│     └─ src
│        └─ main
│           ├─ java
│           │  ├─ com
│           │  │  └─ kjb04
│           │  │     └─ exchange
│           │  │        ├─ Authentication.java
│           │  │        ├─ Main.java
│           │  │        ├─ OnPageCompleteListener.java
│           │  │        ├─ PageCompleter.java
│           │  │        ├─ Parent.java
│           │  │        ├─ api
│           │  │        │  ├─ Exchange.java
│           │  │        │  ├─ ExchangeService.java
│           │  │        │  └─ model
│           │  │        │     ├─ ExchangeRates.java
│           │  │        │     ├─ Offer.java
│           │  │        │     ├─ Token.java
│           │  │        │     ├─ Transaction.java
│           │  │        │     └─ User.java
│           │  │        ├─ login
│           │  │        │  └─ Login.java
│           │  │        ├─ rates
│           │  │        │  └─ Rates.java
│           │  │        ├─ register
│           │  │        │  └─ Register.java
│           │  │        ├─ trading
│           │  │        │  ├─ Trading.java
│           │  │        │  ├─ tradingCreate
│           │  │        │  │  └─ TradingCreate.java
│           │  │        │  └─ tradingOffers
│           │  │        │     └─ TradingOffers.java
│           │  │        └─ transactions
│           │  │           └─ Transactions.java
│           │  └─ module-info.java
│           └─ resources
│              └─ com
│                 └─ kjb04
│                    └─ exchange
│                       ├─ login
│                       │  ├─ login.css
│                       │  └─ login.fxml
│                       ├─ parent.css
│                       ├─ parent.fxml
│                       ├─ rates
│                       │  ├─ rates.css
│                       │  └─ rates.fxml
│                       ├─ register
│                       │  ├─ register.css
│                       │  └─ register.fxml
│                       ├─ trading
│                       │  ├─ trading.css
│                       │  ├─ trading.fxml
│                       │  ├─ tradingCreate
│                       │  │  ├─ tradingCreate.css
│                       │  │  └─ tradingCreate.fxml
│                       │  └─ tradingOffers
│                       │     ├─ tradingOffers.css
│                       │     └─ tradingOffers.fxml
│                       └─ transactions
│                          └─ transactions.fxml
├─ FrontEnd
│  ├─ README.md
│  ├─ package-lock.json
│  ├─ package.json
│  ├─ public
│  │  ├─ favicon.ico
│  │  ├─ index.html
│  │  ├─ logo192.png
│  │  ├─ logo512.png
│  │  ├─ manifest.json
│  │  └─ robots.txt
│  └─ src
│     ├─ App.css
│     ├─ App.js
│     ├─ App.test.js
│     
│     ├─ CreateOffer.js
│     ├─ DrawerNav.js
│     ├─ Home.css
│     ├─ Home.js
│     ├─ Nav.css
│     ├─ Nav.js
│     ├─ Offers.js
│     ├─ UserContext.js
│     ├─ UserCredentialsDialog
│     │  ├─ UserCredentialsDialog.css
│     │  └─ UserCredentialsDialog.js
│     ├─ index.css
│     ├─ index.js
│     ├─ localstorage.js
│     ├─ logo.svg
│     ├─ reportWebVitals.js
│     └─ setupTests.js
└─ README.md

```