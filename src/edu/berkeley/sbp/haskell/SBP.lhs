\begin{code}
--
-- These bindings are highly experimental and subject to change
-- without notice.  You've been warned.
--
-- Warning: this is no longer maintained.  It stopped working in mid-2009.
--
module Edu_Berkeley_Sbp_Haskell_SBP(
  Tree(Tree),Location(Location),Region(Region),parseFile,prettyPrintTree,coalesceFlatHeadlessNodes)
where

#if defined(java_HOST_OS)
import Foreign
import Foreign.Java
#define CONCAT(x,y) x/**/y
#define DEFINE_OBJECT(s,name) \
data CONCAT(name,_); \
type name = JObject CONCAT(name,_); \
foreign import jvm s CONCAT(_,name) :: JClass; \
instance JType_ CONCAT(name,_) where jClass_ _ = CONCAT(_,name);
#else
{-
import Header_Java;
import Class_HaskellHelper;
import Class_java_lang_Object;
import Class_java_lang_Class;
import Class_java_lang_String;
import Class_edu_berkeley_sbp_Tree;
import Header_edu_berkeley_sbp_Tree;
import JVM_HaskellHelper;
import Header_HaskellHelper;
import TypedString;
import JVMBridge;
import JavaText;
import JavaTypes;
import Data.Int;
import Invocation;
-}
import Foreign.JavaVM.Lib.Class_java_lang_System;
import Foreign.JavaVM.Lib.Class_java_lang_String;
import TestCallback_JVM;
import Foreign.JavaVM;
import Platform.JavaVM;
import Data.Witness;
import Control.Concurrent;
import System.IO;
#endif
import Text.PrettyPrint.Leijen

data Location = Location Int Int
data Region   = Region Location Location

data Tree     = Tree String [Tree] Region
instance Show Tree
 where
  show t@(Tree _ _ _) = show $ prettyPrintTree $ t

coalesceFlatHeadlessNodes t@(Tree s children r)
  | s==[], flat t = Tree (concat $ map (\(Tree s _ _) -> s) children) [] r
  | otherwise     = Tree s (map coalesceFlatHeadlessNodes children) r
 where
  flat (Tree _ children _) = not (any id $ map notFlatComponent children)
  notFlatComponent (Tree _ [] _) = False
  notFlatComponent (Tree _ _  _) = True

fsep = fillSep
prettyPrintTree (Tree "" []       _) = empty
prettyPrintTree (Tree s  []       _) = text s
prettyPrintTree (Tree [] children _) = prettyPrintTreeList children
prettyPrintTree (Tree s  children _) = (text (s++":")) <$$> (prettyPrintTreeList children)
prettyPrintTreeList []               = text "{}"
prettyPrintTreeList children   
    | allsingles children = text $ "\"" ++ (concatMap (\(Tree s _ _) -> s) children) ++ "\""
    | otherwise           = hang 2 $
                            (text "{")
                            <+> 
                               (group
                                ((fsep $ map (group . prettyPrintTree) children)
                                 <+>
                                 (text "}")))
allsingles = all (\(Tree s c _) -> (length s)==1 && (length c)==0)

nullRegion = (Region (Location 0 0) (Location 0 0))



------------------------------------------------------------------------------
#if defined(java_HOST_OS)
foreign import jvm type "edu.berkeley.sbp.Tree" JTree#
data JTree = JTree JTree#
foreign import jvm type "java.lang.Object" Object#
data Object = Object Object#
foreign import jvm safe "edu.berkeley.sbp.misc.RegressionTests.main" regressionTests :: IO ()
foreign import jvm safe "HaskellHelper.help" haskellHelper :: JString -> IO JTree
foreign import jvm safe "HaskellHelper.isNull" isNull :: Object -> IO Bool
foreign import jvm safe "getHead" getHead :: JTree -> IO Object
foreign import jvm safe "child" getChild :: JTree -> Int32 -> IO JTree
foreign import jvm safe "size" size :: JTree -> IO Int32
foreign import jvm safe "toString" jtoString :: Object -> IO JString

toString o  = do isn <- isNull o
                 if isn then return ""
                        else do str <- jtoString o
                                return (unpackJString str)

         
haskify :: JTree -> IO Tree
haskify t =
  do head <- getHead t
     str  <- toString head
     numChildren <- size t
     children    <- if numChildren == 0
                        then do return []
                        else do children <- mapM (\i -> getChild t i)
                                              $ take (fromIntegral numChildren)
                                                $ iterate (+1) 0
                                h        <- mapM haskify children
                                return h
     return $ Tree str children nullRegion

parseFile ::
 String   ->   -- file to be parsed
 IO Tree

parseFile f = do f' <- packJString f
                 tree <- haskellHelper f'
                 x <- haskify tree
                 return x

------------------------------------------------------------------------------
#else
  -- Why do I need this?
instance SubJavaClassMarker
    Header_edu_berkeley_sbp_Tree.Class_Jedu_berkeley_sbp_Tree
    Header_HaskellHelper.Class_Jedu_berkeley_sbp_Tree

parseFile ::
   [String] ->   -- class path
   String   ->   -- grammar *.g file
   String   ->   -- file to be parsed
   IO Tree

parseFile classPath grammarFile inputFile =
     runJVM classPath
        ((do class_JHaskellHelper
             s1   <- new_JString_ArrayJchar $ toJavaString grammarFile
             s2   <- new_JString_ArrayJchar $ toJavaString inputFile
             tree <- main_JHaskellHelper_JString_JString (s1, s2)
             t <- haskifyTree tree
             return t
          ) :: JVM Tree)

haskifyTree t = 
    ((do class_JHaskellHelper
         class_JTree
         head <- getHead_JTree t ()
         isNull <- getIsNothing head
         str  <- if isNull
                 then (return "")
                 else (toString_JObject ((castTLRef head) :: Jjava_lang_Object) ()
                                            >>= getStringUTF >>= \x -> return (showUTF8 x))
         numChildren <- size_JTree t()
         children    <- if numChildren == 0
                        then do return []
                        else do children <- mapM (\i -> child_JTree_Jint t ((fromIntegral i)::Int32))
                                              $ take (fromIntegral numChildren)
                                                $ iterate (+1) 0
                                h        <- mapM (\c -> haskifyTree (castTLRef c)) children
                                return h
         return $ Tree str children nullRegion
      ) :: JVM Tree)

#endif
\end{code}
